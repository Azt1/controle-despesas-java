# Próximos passos

Esta lista é um plano de estudo. Nenhuma das funcionalidades abaixo está implementada no esqueleto inicial.

## Primeiro: lançamentos

- Criar uma classe para receita e despesa, com descrição, valor e data.
- Usar `BigDecimal` para os valores monetários.
- Validar descrição obrigatória e valor maior que zero.
- Cadastrar e listar lançamentos, começando sem banco.
- Escrever testes das primeiras regras.

## Depois: organização e consultas

- Adicionar categorias.
- Permitir editar e excluir lançamentos.
- Filtrar por mês e tipo.
- Calcular receitas, despesas e saldo do período.

## Persistência

- Adicionar Spring Data JPA e configurar o banco.
- Salvar e consultar os lançamentos no banco.
- Testar o cadastro e as consultas com persistência.

O desenvolvimento vai ser feito em partes pequenas durante outubro e novembro. O foco é entender cada classe antes de acrescentar novas funcionalidades. Os projetos de biblioteca e candidaturas ficarão em repositórios separados quando forem iniciados.
