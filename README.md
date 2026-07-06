# My Chance — Backend Services

API central do My Chance: perfis anonimizados, vagas, convites e orquestração do matching via serviço NLP.

## Requisitos

- Java 17
- Gradle (wrapper incluso)

## Execução local

```bash
./gradlew bootRun
```

API: `http://localhost:8080`

Perfil **dev** usa H2 em memória. Para recomendações com score de compatibilidade, o serviço **mychance-NLP** deve estar acessível (porta 8000).

### Console H2 (dev)

- `http://localhost:8080/h2-console`
- JDBC: `jdbc:h2:mem:mychance` — usuário `sa`, senha vazia

## Endpoints principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/candidates/profiles` | Cria perfil |
| PUT | `/api/v1/candidates/profiles/{candidatoId}` | Atualiza perfil |
| POST | `/api/v1/jobs` | Cria vaga |
| GET | `/api/v1/jobs/{jobId}/recommendations` | Recomenda candidatos |
| POST | `/api/v1/jobs/{jobId}/invites` | Envia convite |
| POST | `/api/v1/invites/{inviteId}/accept` | Aceita convite |
| POST | `/api/v1/invites/{inviteId}/reject` | Recusa convite |
| GET | `/api/v1/candidates/{candidatoId}/invites` | Lista convites |
| GET | `/health` | Health check |

Detalhes dos contratos JSON: [`docs/api-contracts.md`](docs/api-contracts.md)

## Configuração

```properties
# application.properties (dev)
mychance.nlp.base-url=http://localhost:8000
mychance.nlp.enabled=true
```

Em produção, use as variáveis `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `MYCHANCE_NLP_BASE_URL` e `MYCHANCE_NLP_ENABLED`.

## Produção

1. Aplicar `src/main/resources/schema.sql` no PostgreSQL
2. Subir com perfil `prod`: `./gradlew bootRun --args='--spring.profiles.active=prod'`
3. Garantir que o NLP esteja acessível na URL configurada

## Docker

```bash
docker build -t mychance-backend .
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/mychance \
  -e DATABASE_USERNAME=mychance \
  -e DATABASE_PASSWORD=mychance \
  -e MYCHANCE_NLP_BASE_URL=http://host.docker.internal:8000 \
  mychance-backend
```

## Testes

```bash
./gradlew test
```

## Módulo legado

O diretório `mychance_matching/` contém a versão original do algoritmo em Python. A implementação em uso está no repositório **mychance-NLP**.
