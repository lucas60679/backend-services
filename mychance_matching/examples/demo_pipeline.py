"""
examples/demo_pipeline.py
=========================
Demonstração do pipeline completo de matching do My Chance.

Execução
--------
    python examples/demo_pipeline.py

O script simula um cenário real com uma vaga de Desenvolvedor Back-end
e cinco candidatos com perfis variados, mostrando:

  1. Filtragem por Pontuação Ponderada
  2. Ranqueamento por Cosine Similarity
  3. Resultado final combinado
"""

import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

from weighted_scoring import Vaga, Candidato, rankear_candidatos
from cosine_similarity import construir_matriz_scores


# ---------------------------------------------------------------------------
# Dados de exemplo
# ---------------------------------------------------------------------------

VOCABULARIO = [
    "python", "sql", "docker", "git",
    "react", "java", "powerbi", "fastapi",
]

VAGA = Vaga(
    id=1,
    titulo="Desenvolvedor Back-end Pleno",
    competencias={
        "python":  {"peso": 5, "obrigatoria": True,  "nivel_min": 3},
        "sql":     {"peso": 4, "obrigatoria": True,  "nivel_min": 2},
        "docker":  {"peso": 4, "obrigatoria": False, "nivel_min": 1},
        "git":     {"peso": 3, "obrigatoria": False, "nivel_min": 1},
        "fastapi": {"peso": 3, "obrigatoria": False, "nivel_min": 1},
    },
)

VAGA_DICT = {
    "id": 1,
    "competencias": {c: 1 for c in VAGA.competencias},
    "pesos": {c: cfg["peso"] for c, cfg in VAGA.competencias.items()},
}

CANDIDATOS = [
    Candidato(
        id=1, nome_anonimo="CAND-001",
        competencias={"python": 5, "sql": 4, "docker": 4, "git": 5, "fastapi": 3},
        projetos=3, anos_experiencia=2.0,
    ),
    Candidato(
        id=2, nome_anonimo="CAND-002",
        competencias={"python": 4, "sql": 5, "docker": 2, "git": 4, "react": 3},
        projetos=1, anos_experiencia=4.0,
    ),
    Candidato(
        id=3, nome_anonimo="CAND-003",
        # python abaixo do mínimo → deve ser eliminado na filtragem
        competencias={"python": 1, "sql": 5, "docker": 5, "git": 5, "java": 4},
        projetos=5, anos_experiencia=1.0,
    ),
    Candidato(
        id=4, nome_anonimo="CAND-004",
        competencias={"python": 3, "sql": 2, "docker": 3, "git": 3, "fastapi": 4},
        projetos=0, anos_experiencia=0.5,
    ),
    Candidato(
        id=5, nome_anonimo="CAND-005",
        # sem sql → eliminado na filtragem
        competencias={"python": 5, "docker": 5, "git": 5, "react": 5, "fastapi": 5},
        projetos=10, anos_experiencia=0.0,
    ),
]


# ---------------------------------------------------------------------------
# Helpers de exibição
# ---------------------------------------------------------------------------

SEP = "─" * 60


def cabecalho(titulo: str) -> None:
    print(f"\n{'═' * 60}")
    print(f"  {titulo}")
    print('═' * 60)


def linha(label: str, valor) -> None:
    print(f"  {label:<30} {valor}")


# ---------------------------------------------------------------------------
# Pipeline
# ---------------------------------------------------------------------------

def etapa_1_filtragem(candidatos: list[Candidato], vaga: Vaga) -> tuple[list, list]:
    """Pontuação Ponderada: separa aprovados de eliminados."""
    cabecalho("ETAPA 1 — Pontuação Ponderada (Filtragem)")
    print(f"  Vaga: {vaga.titulo}\n")

    ranking_completo = rankear_candidatos(candidatos, vaga, apenas_aprovados=False)
    aprovados = []
    eliminados = []

    for r in ranking_completo:
        status = "✔ APROVADO" if r["aprovado_filtragem"] else "✘ ELIMINADO"
        print(f"  {r['nome_anonimo']}  |  Score: {r['score_final']:.3f}"
              f"  |  Ausências obrig.: {r['ausencias_obrigatorias']}"
              f"  |  {status}")
        if r["aprovado_filtragem"]:
            aprovados.append(r)
        else:
            eliminados.append(r)

    print(f"\n  {SEP}")
    print(f"  Aprovados: {len(aprovados)}  |  Eliminados: {len(eliminados)}")
    return aprovados, eliminados


def etapa_2_ranking(aprovados_ids: list[int], candidatos: list[Candidato],
                     vaga_dict: dict) -> None:
    """Cosine Similarity: ranking fino dos candidatos aprovados."""
    cabecalho("ETAPA 2 — Cosine Similarity (Ranking Final)")

    candidatos_dict = [
        {
            "id": c.nome_anonimo,
            "competencias": c.competencias,
        }
        for c in candidatos
        if c.id in aprovados_ids
    ]

    df = construir_matriz_scores(candidatos_dict, vaga_dict, VOCABULARIO)

    print(df.to_string())
    print(f"\n  {SEP}")
    if not df.empty:
        melhor = df.iloc[0]
        print(f"  Melhor candidato: {melhor['candidato_id']}"
              f"  |  Similaridade: {melhor['similaridade']:.4f}"
              f"  |  {melhor['compatibilidade']}")


def main() -> None:
    print("\n" + "█" * 60)
    print("  MY CHANCE — Demo do Pipeline de Matching")
    print("█" * 60)

    # Etapa 1
    aprovados, eliminados = etapa_1_filtragem(CANDIDATOS, VAGA)
    aprovados_ids = {
        next(c.id for c in CANDIDATOS if c.nome_anonimo == r["nome_anonimo"])
        for r in aprovados
    }

    # Etapa 2
    etapa_2_ranking(aprovados_ids, CANDIDATOS, VAGA_DICT)

    # Sumário
    cabecalho("SUMÁRIO FINAL")
    linha("Candidatos avaliados:", len(CANDIDATOS))
    linha("Aprovados na filtragem:", len(aprovados))
    linha("Eliminados na filtragem:", len(eliminados))
    print()


if __name__ == "__main__":
    main()
