# Controle de despesas pessoais

Projeto de estudo em Java para acompanhar receitas e despesas pessoais. A ideia é registrar os lançamentos, separar por categoria e consultar o saldo do mês.

O projeto está no começo. Já é possível cadastrar e consultar lançamentos em memória pelas classes Java, filtrar por tipo ou mês e calcular o saldo mensal. Ainda não há cadastro pela API, banco de dados, autenticação ou telas.

O valor usa `BigDecimal` e precisa ser maior que zero, tanto para receitas quanto para despesas. O tipo identifica se o dinheiro entra ou sai. Descrição, data e tipo são obrigatórios; espaços nas pontas da descrição são removidos. Por enquanto, a classe só permite consultar os dados depois de criada.

## O que funciona hoje

- Cadastro e listagem de lançamentos por meio de `LancamentoService`.
- Consulta de receitas ou despesas com `listarPorTipo`.
- Consulta de um mês e ano com `listarPorMes`, usando `YearMonth`.
- Saldo mensal com `calcularSaldo`: receitas menos despesas. Meses sem movimento retornam zero, e o saldo pode ser negativo.

Os dados ficam numa lista em memória e são perdidos ao reiniciar a aplicação. As consultas devolvem cópias da lista, para que limpar um resultado não apague os dados internos. Os métodos ainda não estão disponíveis por HTTP; por enquanto, são usados nos testes.

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Maven, com Maven Wrapper incluído
- JUnit para testes

## Executar

É necessário ter o JDK 17 ou superior instalado e a variável `JAVA_HOME` apontando para a pasta do JDK. Não é necessário instalar o Maven separadamente; o Wrapper baixa a versão do projeto na primeira execução. Essa etapa precisa de internet.

No PowerShell, dentro da pasta do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Se o JDK 17 estiver instalado no caminho padrão deste computador, você pode definir a variável apenas para o terminal atual:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd spring-boot:run
```

A aplicação inicia na porta 8080. Ainda não existem rotas de negócio: abrir `http://localhost:8080` e receber 404 é esperado nesta versão. Para encerrar, pressione `Ctrl+C` no terminal.

No Linux ou macOS, use `./mvnw spring-boot:run`.

## Testar

```powershell
.\mvnw.cmd test
```

Os testes verificam a inicialização sem banco, os campos obrigatórios, os valores positivos, o armazenamento em memória, os filtros e o saldo mensal. Também cobrem mudanças de ano, saldo negativo e cálculos com centavos. Para executar os testes e gerar o arquivo `.jar`, use `.\mvnw.cmd verify`. O GitHub Actions está configurado para executar essa verificação a cada push e pull request.

## Organização

- `controller`: receberá as requisições da API.
- `model`: contém `Lancamento` e `TipoLancamento`; categorias serão adicionadas depois.
- `service`: contém o cadastro, as consultas e o cálculo de saldo.
- `repository`: guarda os lançamentos em memória e aplica os filtros.
- `src/main/resources`: configurações da aplicação.
- `src/test/java`: testes automatizados.

A pasta `controller` ainda está vazia e usa `.gitkeep` somente para aparecer no Git. Ainda não há API de lançamentos nem acesso ao banco.

O [plano de desenvolvimento](docs/proximos-passos.md) lista as próximas funcionalidades.
