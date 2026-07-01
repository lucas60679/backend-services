# My Chance — Motor de Matching

> Módulo de algoritmos de ranqueamento anônimo de candidatos por competências técnicas.

### Por (is)Rael Buriti, 122111123.

---

## Estrutura do projeto

```
mychance_matching/
├── weighted_scoring.py          # Algoritmo 1 — Pontuação Ponderada
├── cosine_similarity.py         # Algoritmo 2 — Cosine Similarity
├── __init__.py
├── requirements.txt
├── examples/
│   └── demo_pipeline.py         # Demo do pipeline completo
└── tests/
    ├── conftest.py
    ├── test_weighted_scoring.py  # 22 testes unitários
    └── test_cosine_similarity.py # 22 testes unitários
```

---

## Instalação

```bash
pip install -r requirements.txt
```

---

## Como funciona o pipeline

O matching acontece em **duas etapas** sequenciais:

```
Todos os candidatos
       │
       ▼
┌─────────────────────────┐
│  Etapa 1                │
│  Pontuação Ponderada    │  → Elimina quem não atende competências obrigatórias
│  (weighted_scoring.py)  │  → Ordena por score ponderado
└─────────────────────────┘
       │ aprovados
       ▼
┌─────────────────────────┐
│  Etapa 2                │
│  Cosine Similarity      │  → Ranking final por similaridade vetorial
│  (cosine_similarity.py) │  → Score de 0.0 (sem sobreposição) a 1.0 (perfeito)
└─────────────────────────┘
       │
       ▼
  Top-N candidatos ranqueados
```

---

## Algoritmo 1 — Pontuação Ponderada (`weighted_scoring.py`)

### Fórmula

```
Score_base  = Σ (peso_i × nivel_i)
Score_norm  = Score_base / Score_máximo             ∈ [0, 1]
Score_final = (Score_norm + bônus_projetos) × fator_recência
              − penalização_obrigatórias
```

### Uso

```python
from weighted_scoring import Vaga, Candidato, rankear_candidatos

vaga = Vaga(
    id=1,
    titulo="Dev Back-end",
    competencias={
        "python": {"peso": 5, "obrigatoria": True,  "nivel_min": 3},
        "sql":    {"peso": 4, "obrigatoria": True,  "nivel_min": 2},
        "docker": {"peso": 3, "obrigatoria": False, "nivel_min": 1},
    },
)

candidatos = [
    Candidato(1, "CAND-001", {"python": 4, "sql": 5, "docker": 3}, projetos=2, anos_experiencia=1.0),
    Candidato(2, "CAND-002", {"python": 1, "sql": 5, "docker": 5}, projetos=5, anos_experiencia=0.5),
]

# Retorna apenas aprovados (sem lacunas obrigatórias), ordenados por score
ranking = rankear_candidatos(candidatos, vaga)

for pos, r in enumerate(ranking, 1):
    print(f"{pos}. {r['nome_anonimo']} | Score: {r['score_final']:.3f} | Aprovado: {r['aprovado_filtragem']}")
```

### Parâmetros do `Candidato`

| Campo              | Tipo    | Descrição                                      |
|--------------------|---------|------------------------------------------------|
| `id`               | `int`   | Identificador interno                          |
| `nome_anonimo`     | `str`   | ID público exibido no My Chance (ex: CAND-042) |
| `competencias`     | `dict`  | `{"competencia": nivel}` em escala 0–5         |
| `projetos`         | `int`   | Projetos reais no portfólio (bônus)            |
| `anos_experiencia` | `float` | Usado no fator de recência (decaimento exp.)   |

### Parâmetros da `Vaga`

| Campo na competência | Tipo    | Descrição                                  |
|----------------------|---------|--------------------------------------------|
| `peso`               | `float` | Importância relativa (quanto maior, mais)  |
| `obrigatoria`        | `bool`  | Se ausente, elimina o candidato            |
| `nivel_min`          | `int`   | Nível mínimo aceitável quando obrigatória  |

---

## Algoritmo 2 — Cosine Similarity (`cosine_similarity.py`)

### Fórmula

```
cos(θ) = (A · B) / (||A|| × ||B||)

Versão ponderada:
cos_w(θ) = Σ(w_i × A_i × B_i) / (√(Σ w_i A_i²) × √(Σ w_i B_i²))
```

### Uso

```python
from cosine_similarity import construir_matriz_scores

VOCABULARIO = ["python", "sql", "docker", "git", "react"]

vaga = {
    "id": 1,
    "competencias": {"python": 1, "sql": 1, "docker": 1, "git": 1},
    "pesos":        {"python": 5, "sql": 4, "docker": 3, "git": 2},
}

candidatos = [
    {"id": "CAND-001", "competencias": {"python": 4, "sql": 3, "docker": 2, "git": 5}},
    {"id": "CAND-002", "competencias": {"python": 5, "sql": 5, "docker": 5, "git": 5}},
]

df = construir_matriz_scores(candidatos, vaga, VOCABULARIO, top_n=5)
print(df)
```

**Saída:**
```
         candidato_id  similaridade  angulo_graus compatibilidade
posicao
1            CAND-002        1.0000          0.00       Excelente
2            CAND-001        0.9823          10.8       Excelente
```

### Interpretação da similaridade

| Similaridade      | Ângulo    | Rótulo         |
|-------------------|-----------|----------------|
| 0.95 – 1.00       | < 18°     | Excelente      |
| 0.85 – 0.94       | < 32°     | Alta           |
| 0.60 – 0.84       | < 53°     | Moderada       |
| 0.30 – 0.59       | < 73°     | Baixa          |
| 0.00 – 0.29       | ≥ 73°     | Muito baixa    |

---

## Demo completo

```bash
python examples/demo_pipeline.py
```

**Saída esperada:**
---
  MY CHANCE — Demo do Pipeline de Matching
---
```
════════════════════════════════════════════════════════════
  ETAPA 1 — Pontuação Ponderada (Filtragem)
════════════════════════════════════════════════════════════
  Vaga: Desenvolvedor Back-end Pleno

  CAND-005  |  Score: 0.842  |  Ausências obrig.: 1  |  ✘ ELIMINADO
  CAND-001  |  Score: 0.750  |  Ausências obrig.: 0  |  ✔ APROVADO
  CAND-004  |  Score: 0.561  |  Ausências obrig.: 0  |  ✔ APROVADO
  CAND-003  |  Score: 0.509  |  Ausências obrig.: 1  |  ✘ ELIMINADO
  CAND-002  |  Score: 0.438  |  Ausências obrig.: 0  |  ✔ APROVADO

  Aprovados: 3  |  Eliminados: 2

════════════════════════════════════════════════════════════
  ETAPA 2 — Cosine Similarity (Ranking Final)
════════════════════════════════════════════════════════════
         candidato_id  similaridade  angulo_graus compatibilidade
posicao
1            CAND-001        0.9863          9.51       Excelente
2            CAND-004        0.9796         11.59       Excelente
3            CAND-002        0.8812         28.21            Alta

  Melhor candidato: CAND-001  |  Similaridade: 0.9863  |  Excelente
```

---

## Testes

```bash
# Rodar todos os testes
pytest tests/ -v

# Com cobertura
pytest tests/ -v --cov=. --cov-report=term-missing
```

**44 testes cobrindo:**
- `calcular_fator_recencia` — decaimento, bordas, negativos
- `calcular_score_ponderado` — fórmula, bônus, penalizações, ausências
- `rankear_candidatos` — ordenação, filtro, lista vazia
- `vetorizar_perfil` — alinhamento, zeros, tipos
- `cosine_similarity` — identidade, ortogonalidade, invariância a escala, pesos, erros
- `construir_matriz_scores` — ordenação, top-n, DataFrame vazio

---

## Integração com o restante do My Chance

```python
# No back-end (FastAPI / Python puro)
from weighted_scoring import Vaga, Candidato, rankear_candidatos
from cosine_similarity import construir_matriz_scores

def endpoint_sugestoes(vaga_id: int, db_session):
    vaga   = db_session.query(Vaga).get(vaga_id)        # do banco
    todos  = db_session.query(Candidato).all()           # do banco

    aprovados = rankear_candidatos(todos, vaga)
    ids_aprovados = [r["candidato_id"] for r in aprovados]

    candidatos_dict = [{"id": c.id, "competencias": c.competencias}
                       for c in todos if c.id in ids_aprovados]

    ranking = construir_matriz_scores(candidatos_dict, vaga_dict, VOCABULARIO)
    return ranking.to_dict(orient="records")
```

---

## Dependências

| Pacote      | Versão mínima | Uso                          |
|-------------|---------------|------------------------------|
| `numpy`     | 1.24.0        | Vetores e álgebra linear     |
| `pandas`    | 2.0.0         | DataFrame de ranking         |
| `pytest`    | 7.4.0         | Testes unitários             |
| `pytest-cov`| 4.1.0         | Cobertura de testes          |
