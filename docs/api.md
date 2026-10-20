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

Se não houver cadastro, a resposta JSON é `[]`. Os filtros e o cálculo de saldo ainda estão disponíveis somente nas classes Java, não nessa rota.

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
