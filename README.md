# My Chance — Backend Services

Spring Boot backend for the **My Chance** recruitment platform. It stores anonymized candidate profiles, job vacancies, and exposes REST APIs aligned with the Streamlit front-end.

## Stack

- Java 17
- Spring Boot 4 (Web, Data JPA, Validation)
- **H2** in-memory database for local development
- **PostgreSQL** profile for production (`application-prod.properties`)

## Quick start

```bash
./gradlew bootRun
```

The app starts on `http://localhost:8080`.

### H2 console (dev only)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:mychance`
- User: `sa` / Password: *(empty)*

On startup, a sample job vacancy is seeded. Fetch its ID:

```bash
curl http://localhost:8080/api/v1/dev/sample-job-id
```

## API

### Create anonymous candidate profile

`POST /api/v1/candidates/profiles`

```json
{
  "competencias": {
    "python": 4,
    "sql": 5,
    "docker": 2,
    "powerbi": 0
  },
  "experiencias": [
    { "cargo": "Desenvolvedor Backend Júnior", "tempo_meses": 14 },
    { "cargo": "Estagiário de Dados", "tempo_meses": 6 }
  ],
  "projetos_destaque": [
    "Desenvolvimento de API de e-commerce utilizando Python e Docker."
  ]
}
```

### Get job recommendations

`GET /api/v1/jobs/{jobId}/recommendations`

Returns ranked anonymous candidates with compatibility scores.

## Architecture highlights

| Layer | Responsibility |
|-------|----------------|
| **Entities** | Relational model: candidates, anonymous profiles, skills, projects, experiences, jobs |
| **MatchingAdapter** | Converts DB records into scalar payloads (`anos_experiencia`, `projetos`, skill weights) for the ML engine |
| **MatchingEngine** | Weighted cosine similarity + experience/project bonuses (Java placeholder until Python integration) |
| **TextSanitizerService** | Strips emails and social/URL links from free-text project descriptions |

### Allowed skills (closed vocabulary)

`python`, `sql`, `docker`, `powerbi`, `streamlit`, `postgresql`

## Production (PostgreSQL)

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

Environment variables:

- `DATABASE_URL` (default: `jdbc:postgresql://localhost:5432/mychance`)
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

Apply the reference DDL in `src/main/resources/schema.sql` before running with `ddl-auto=validate`.

## Tests

```bash
./gradlew test
```
