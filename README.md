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

## Publicação gratuita

O arquivo `render.yaml` deixa a API pronta para publicação no Render usando o Dockerfile do projeto. No painel do Render, informe as credenciais do Neon sem gravá-las no repositório:

- `DATABASE_URL`: URL JDBC no formato `jdbc:postgresql://HOST/BANCO?sslmode=require`;
- `DATABASE_USER`: usuário fornecido pelo Neon;
- `DATABASE_PASSWORD`: senha fornecida pelo Neon;
- `JWT_SECRET`: gerado automaticamente pelo Render;
- `ADMIN_EMAIL`: e-mail da conta que receberá o papel administrativo após o cadastro.

O plano gratuito pode suspender a API durante inatividade, portanto a primeira requisição pode demorar mais. Para uso comercial com disponibilidade contínua, migre o serviço para um plano sem suspensão.

Desenvolvido por Dev Rodrigo • Todos os direitos reservados.
