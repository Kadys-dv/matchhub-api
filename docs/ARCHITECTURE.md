# Arquitetura da MatchHub API

```mermaid
flowchart LR
  C[Cliente] --> S[Spring Security]
  S --> CT[Controllers REST]
  CT --> SV[Serviços transacionais]
  SV --> J[JPA / Repositórios]
  J --> P[(PostgreSQL)]
  F[Flyway] --> P
  A[Actuator] --> H[Liveness / Readiness / Métricas]
```

JWT autentica a identidade e os papéis são verificados no servidor. Criação, entrada, saída, conclusão e moderação passam pela camada de serviços. O bloqueio de partida protege a última vaga contra concorrência.
