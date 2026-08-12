# MatchHub API

API REST real para criação e gerenciamento seguro de partidas esportivas, desenvolvida com Java 21 e Spring Boot 4.

## O que já funciona

- cadastro e login com senha BCrypt e JWT;
- criação e listagem paginada de partidas;
- entrada e desistência de jogadores;
- controle transacional de vagas com bloqueio pessimista;
- conclusão restrita ao organizador;
- PostgreSQL, migrations Flyway e validação de dados;
- erros HTTP padronizados, Actuator e OpenAPI/Swagger;
- testes de integração e CI no GitHub Actions.

## Executar localmente

Com Docker instalado, gere uma chave Base64 segura, defina `JWT_SECRET` e execute:

```bash
./mvnw clean package
docker compose up --build
```

Swagger: `http://localhost:8080/swagger-ui.html`  
Saúde: `http://localhost:8080/actuator/health`

Sem Docker, configure `DATABASE_URL`, `DATABASE_USER` e `DATABASE_PASSWORD` para um PostgreSQL disponível e execute `mvnw.cmd spring-boot:run` no Windows.

## Fluxo principal

1. `POST /api/v1/auth/register`
2. Copie o `accessToken` retornado.
3. Envie `Authorization: Bearer <token>`.
4. `POST /api/v1/matches` para criar.
5. `POST /api/v1/matches/{id}/participants/me` para entrar.
6. `DELETE /api/v1/matches/{id}/participants/me` para desistir.
7. `POST /api/v1/matches/{id}/complete` para o organizador concluir.

## Segurança

O segredo padrão serve somente para desenvolvimento. Em produção, `JWT_SECRET` deve ser longo, aleatório, codificado em Base64 e armazenado no gerenciador de segredos da plataforma.
