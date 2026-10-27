# Próximos passos

Esta lista acompanha o desenvolvimento. Os itens marcados já estão implementados; os demais são os próximos passos.

## Primeiro: lançamentos

- [x] Criar uma classe para receita e despesa, com descrição, valor e data.
- [x] Usar `BigDecimal` para os valores monetários.
- [x] Validar descrição obrigatória e valor maior que zero.
- [x] Cadastrar e listar lançamentos em memória, por meio das classes Java.
- [x] Escrever testes das primeiras regras.
- [x] Consultar lançamentos por tipo ou mês e ano.
- [x] Calcular o saldo de um mês e ano.
- [x] Expor o cadastro e a listagem pela API.
- [x] Tratar campos inválidos e erros de leitura do JSON com HTTP 400.
- [x] Testar as rotas de cadastro e listagem.
- [x] Expor o saldo mensal pela API, com validação do mês e testes HTTP.
- [x] Expor os filtros da listagem pela API, separados ou combinados.

## Depois: organização e consultas

- Adicionar categorias.
- Permitir editar e excluir lançamentos.
- [x] Combinar os filtros por mês e tipo numa mesma consulta.
- Consultar separadamente os totais de receitas e despesas do período.

## Persistência

- Adicionar Spring Data JPA e configurar o banco.
- Salvar e consultar os lançamentos no banco.
- Testar o cadastro e as consultas com persistência.

O desenvolvimento vai ser feito em partes pequenas durante outubro e novembro. O foco é entender cada classe antes de acrescentar novas funcionalidades. Os projetos de biblioteca e candidaturas ficarão em repositórios separados quando forem iniciados.
