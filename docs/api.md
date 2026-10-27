# Testar a API

Inicie a aplicação na pasta do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Deixe esse terminal aberto e use outro para os comandos abaixo.

## Cadastrar um lançamento

`POST /api/lancamentos` recebe descrição, valor, data e tipo. A data usa o formato `yyyy-MM-dd`, e o tipo deve ser `RECEITA` ou `DESPESA`.

```powershell
$corpo = @{
    descricao = "Mercado"
    valor = 42.50
    data = "2026-10-20"
    tipo = "DESPESA"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "http://localhost:8080/api/lancamentos" `
    -ContentType "application/json; charset=utf-8" `
    -Body ([System.Text.Encoding]::UTF8.GetBytes($corpo))
```

O cadastro retorna HTTP `201` com o lançamento criado:

```json
{
  "descricao": "Mercado",
  "valor": 42.50,
  "data": "2026-10-20",
  "tipo": "DESPESA"
}
```

O valor precisa ser maior que zero. O tipo define se é entrada ou saída; não envie despesas como números negativos. A descrição não pode ficar vazia, e a data e o tipo são obrigatórios.

## Consultar os lançamentos

`GET /api/lancamentos` retorna HTTP `200` e uma lista com todos os lançamentos cadastrados:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/lancamentos"
```

Se não houver cadastro, a resposta JSON é `[]`.

## Filtrar os lançamentos

A listagem aceita os parâmetros opcionais `mes` e `tipo`. Use `yyyy-MM` para o mês e `RECEITA` ou `DESPESA` para o tipo, em letras maiúsculas.

Para consultar somente as despesas:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/lancamentos?tipo=DESPESA"
```

Para consultar todos os lançamentos de outubro de 2026:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/lancamentos?mes=2026-10"
```

Os dois filtros podem ser usados juntos. Este exemplo retorna somente as despesas de outubro de 2026:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/lancamentos?mes=2026-10&tipo=DESPESA"
```

Sem parâmetros, a rota continua listando todos os lançamentos. Se nenhum lançamento corresponder aos filtros, retorna HTTP `200` com `[]`. O filtro por mês também considera o ano, e a consulta não altera os dados.

Um mês como `2026-13` retorna HTTP `400`, com orientação sobre o formato. Um tipo como `OUTRO` retorna `400` com esta resposta:

```json
{
  "mensagem": "Informe o tipo como RECEITA ou DESPESA."
}
```

## Consultar o saldo mensal

`GET /api/lancamentos/saldo?mes=2026-10` retorna HTTP `200`, com o mês consultado e o saldo: receitas menos despesas daquele mês e ano.

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/lancamentos/saldo?mes=2026-10"
```

Se você cadastrou apenas a despesa de R$ 42,50 do exemplo anterior, o resultado é:

```json
{
  "mes": "2026-10",
  "saldo": -42.50
}
```

O parâmetro `mes` é obrigatório e usa o formato `yyyy-MM`. Meses sem lançamentos retornam saldo zero. A consulta não altera os lançamentos e não inclui valores de outros meses ou anos.

Mês ausente ou inválido, como `2026-13`, retorna HTTP `400`:

```json
{
  "mensagem": "Informe o mês no formato yyyy-MM, por exemplo 2026-10."
}
```

## Respostas de erro

Campos inválidos retornam HTTP `400`. Por exemplo, um cadastro com valor zero recebe:

```json
{
  "mensagem": "O valor deve ser maior que zero."
}
```

JSON malformado, corpo ausente, data inválida ou tipo diferente de `RECEITA` e `DESPESA` também retornam `400`, com uma orientação para corrigir o corpo da requisição.

## Limitações

Os dados ficam em memória e desaparecem ao reiniciar. Ainda não há identificadores, edição, exclusão ou autenticação. Use dados fictícios e execute apenas em ambiente local de estudo; esta versão não está pronta para exposição pública.
