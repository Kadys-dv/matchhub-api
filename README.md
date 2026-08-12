# MatchHub API

API REST da plataforma PlayMatch para gerenciar usuários, partidas, participações, denúncias e indicadores administrativos com segurança e consistência transacional.

## Recursos

- Cadastro, login, BCrypt e autenticação JWT
- Papéis `PLAYER` e `ADMIN` com autorização no servidor
- Criação, participação, desistência, conclusão e cancelamento de partidas
- Controle concorrente de vagas com bloqueio pessimista
- Consulta dos participantes confirmados
- Gestão administrativa de contas ativas e desativadas
- Denúncias com fila de moderação e resolução
- Indicadores consolidados para o dashboard
- PostgreSQL, Flyway, OpenAPI, Actuator e Docker
- Quatro testes de integração cobrindo autenticação, partidas e administração

## Executar

Copie `.env.example` para `.env`, gere um segredo Base64 seguro e configure `ADMIN_EMAIL` somente se desejar promover uma conta previamente cadastrada.

```powershell
docker compose up --build
```

- Swagger: <http://localhost:8080/swagger-ui.html>
- Saúde: <http://localhost:8080/actuator/health>

## Qualidade

```powershell
.\mvnw.cmd test
```

O controle de autorização é aplicado pela API; o frontend nunca é considerado uma fronteira de segurança.

Desenvolvido por Dev Rodrigo • Todos os direitos reservados.
