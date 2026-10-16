# Controle de despesas pessoais

Projeto de estudo em Java para acompanhar receitas e despesas pessoais. A ideia é registrar os lançamentos, separar por categoria e consultar o saldo do mês.

O projeto está no começo: já tem a estrutura Spring Boot e a classe que representa um lançamento, com descrição, valor, data e tipo (receita ou despesa). Ainda não há cadastro pela API, banco de dados, autenticação ou telas.

O valor usa `BigDecimal` e precisa ser maior que zero, tanto para receitas quanto para despesas. O tipo identifica se o dinheiro entra ou sai. Descrição, data e tipo são obrigatórios; espaços nas pontas da descrição são removidos. Por enquanto, a classe só permite consultar os dados depois de criada.

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

Os testes verificam a inicialização sem banco e as regras de `Lancamento`: campos obrigatórios, valor positivo, remoção de espaços na descrição e preservação do valor em `BigDecimal`. Para executar os testes e gerar o arquivo `.jar`, use `.\mvnw.cmd verify`. O GitHub Actions está configurado para executar essa verificação a cada push e pull request.

## Organização

- `controller`: receberá as requisições da API.
- `model`: contém `Lancamento` e `TipoLancamento`; categorias serão adicionadas depois.
- `service`: receberá as regras de negócio.
- `repository`: receberá o acesso aos dados.
- `src/main/resources`: configurações da aplicação.
- `src/test/java`: testes automatizados.

As pastas `controller`, `service` e `repository` ainda estão vazias e usam `.gitkeep` somente para aparecerem no Git. Ainda não há API de lançamentos nem acesso ao banco.

O [plano de desenvolvimento](docs/proximos-passos.md) lista as próximas funcionalidades.
