# MatchHub API

[![Java](https://img.shields.io/badge/Java-21-0f172a?style=for-the-badge)](#stack)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](#stack)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Flyway-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](#stack)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](#executar-localmente)

API REST da plataforma PlayMatch para gerenciar usuarios, partidas, participacoes, denuncias e indicadores administrativos com seguranca, consistencia transacional e regras de negocio no servidor.

## Demo e documentacao

- Estudo de caso: <https://kadys-dv.github.io/portfolio-rodrigo/projetos/matchhub-api.html>
- Repositorio: <https://github.com/Kadys-dv/matchhub-api>
- Swagger local: <http://localhost:8080/swagger-ui.html>
- Health local: <http://localhost:8080/actuator/health>
- Arquitetura: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Problema

O PlayMatch precisa controlar operacoes sensiveis de partidas: cadastro, login, participacao, desistencias, lotacao, denuncias, moderacao e indicadores. Essas regras nao podem depender apenas do aplicativo ou do painel web, porque clientes diferentes podem tentar executar acoes concorrentes ou nao autorizadas.

## Solucao

A MatchHub API centraliza as regras criticas em um backend Java com autenticacao, autorizacao, persistencia relacional e transacoes. O frontend consome a API, mas a decisao final de seguranca e consistencia permanece no servidor.

## Funcionalidades

- Cadastro, login, BCrypt e autenticacao JWT.
- Papeis `PLAYER` e `ADMIN` com autorizacao no servidor.
- Criacao, participacao, desistencias, conclusao e cancelamento de partidas.
- Controle concorrente de vagas com bloqueio pessimista.
- Consulta de participantes confirmados.
- Gestao administrativa de contas ativas e desativadas.
- Denuncias com fila de moderacao e resolucao.
- Indicadores consolidados para dashboard administrativo.
- Migrations com Flyway, documentacao OpenAPI, Actuator e Docker.
- Testes de integracao cobrindo autenticacao, partidas e administracao.

## Stack

- Java 21
- Spring Boot 4.x
- Spring Security e OAuth2 Resource Server
- PostgreSQL
- Flyway
- JWT
- Docker e Docker Compose
- OpenAPI/Swagger
- Actuator
- JUnit, Spring Boot Test, H2 e JaCoCo

## Executar localmente

Copie `.env.example` para `.env`, gere um segredo Base64 seguro e configure `ADMIN_EMAIL` somente se desejar promover uma conta previamente cadastrada.

```powershell
docker compose up --build
```

Depois acesse:

- Swagger: <http://localhost:8080/swagger-ui.html>
- Saude: <http://localhost:8080/actuator/health>

## Qualidade

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify
```

`mvnw verify` tambem gera o relatorio JaCoCo em `target/site/jacoco/index.html`.

## Seguranca

- O controle de autorizacao e aplicado pela API; o frontend nunca e considerado fronteira de seguranca.
- Senhas sao persistidas com BCrypt.
- Credenciais de banco, segredo JWT e e-mail administrativo devem ficar apenas em variaveis de ambiente.
- `.env.example` documenta a configuracao sem versionar segredos.

## Publicacao

O arquivo `render.yaml` deixa a API pronta para publicacao no Render usando o Dockerfile do projeto. No painel do Render, informe as credenciais do Neon sem grava-las no repositorio:

- `DATABASE_URL`: URL JDBC no formato `jdbc:postgresql://HOST/BANCO?sslmode=require`;
- `DATABASE_USER`: usuario fornecido pelo Neon;
- `DATABASE_PASSWORD`: senha fornecida pelo Neon;
- `JWT_SECRET`: gerado automaticamente pelo Render;
- `ADMIN_EMAIL`: e-mail da conta que recebera o papel administrativo apos o cadastro.

O plano gratuito pode suspender a API durante inatividade, portanto a primeira requisicao pode demorar mais. Para uso comercial com disponibilidade continua, use um plano sem suspensao.

Desenvolvido por Dev Rodrigo. Todos os direitos reservados.
