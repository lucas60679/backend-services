# Contratos de API — My Chance

> **Fonte da verdade:** `backend-services`. Os demais repositórios devem espelhar estes contratos.

## Vocabulário fechado de competências (34 skills)

Chaves JSON em minúsculas/snake_case. Níveis de proficiência: `0` a `5`.

Principais grupos: linguagens (`python`, `java`, `javascript`…), dados (`sql`, `postgresql`, `mongodb`…), front-end (`react`, `angular`, `vue`…), back-end (`spring`, `django`, `fastapi`…), DevOps (`docker`, `kubernetes`, `aws`…), analytics (`powerbi`, `data_analysis`, `machine_learning`).

Lista completa: enum `SkillName` em `backend-services`.

## Metadados anonimizados do candidato

| Campo | Valores permitidos |
|-------|-------------------|
| `nivel_escolaridade` | `ensino_medio_completo`, `tecnico`, `graduacao_andamento`, `graduacao_concluida`, `pos_graduacao_andamento`, `pos_graduacao_concluida` |
| `estado` | UF (`ac`…`to`), ex.: `pb`, `sp` |

## Salário (dois campos distintos)

| Campo | Quem informa | Significado |
|-------|--------------|-------------|
| `pretensao_salarial_minima` | **Candidato** | Valor mínimo mensal (R$) que aceita receber |
| `salario_maximo` | **Recrutador / vaga** | Teto orçamentário mensal (R$) da posição |

Regra de matching: candidatos com `pretensao_salarial_minima > salario_maximo` são excluídos **antes** do NLP ranquear.

## Perfil anonimizado

### `POST /api/v1/candidates/profiles`

### `PUT /api/v1/candidates/profiles/{candidatoId}`

```json
{
  "competencias": { "python": 4, "sql": 5 },
  "experiencias": [{ "cargo": "Desenvolvedor Backend", "senioridade": "junior", "inicio_mes": 1, "inicio_ano": 2024, "atual": true }],
  "projetos_destaque": ["Descrição limitada a tecnologias, sem links ou e-mails."],
  "nivel_escolaridade": "graduacao_concluida",
  "estado": "pb",
  "pretensao_salarial_minima": 6500
}
```

## Vagas

### `POST /api/v1/jobs`

```json
{
  "titulo": "Desenvolvedor Backend Python",
  "recrutador_id": "00000000-0000-0000-0000-000000000001",
  "descricao": "Atuação em APIs e integrações.",
  "salario_maximo": 12000,
  "requisitos": [
    { "competencia": "python", "peso": 5, "obrigatoria": true, "nivel_min": 3 }
  ]
}
```

## Matching / recomendações

O **score e o rótulo `compatibilidade` vêm exclusivamente do serviço NLP** (`mychance-NLP`). Se o NLP estiver indisponível, `GET /jobs/{id}/recommendations` retorna `503`.

### `GET /api/v1/jobs/{jobId}/recommendations`

Resposta:

```json
[
  {
    "candidato_id": "usr_a1b2c3d4",
    "compatibilidade_score": 0.9863,
    "compatibilidade": "Excelente",
    "competencias_tecnicas": ["Python", "SQL"],
    "experiencias": [{ "cargo": "Desenvolvedor Backend Júnior", "tempo_meses": 14 }],
    "projetos_destaque": ["..."]
  }
]
```

Rótulos NLP: `Excelente`, `Alta`, `Moderada`, `Baixa`, `Muito baixa`, `Eliminado`.

### `POST /api/v1/matching/rank` (mychance-NLP)

Contrato interno entre backend e NLP — ver README do repositório NLP.

## Convites

Estados: `SUGERIDO` → `ENVIADO` → `ACEITO` | `RECUSADO` | `INVALIDADO`

| Método | Endpoint |
|--------|----------|
| POST | `/api/v1/jobs/{jobId}/invites` |
| POST | `/api/v1/invites/{inviteId}/accept` |
| POST | `/api/v1/invites/{inviteId}/reject` |
| GET | `/api/v1/candidates/{candidatoId}/invites` |
| GET | `/api/v1/jobs/{jobId}/invites` |
