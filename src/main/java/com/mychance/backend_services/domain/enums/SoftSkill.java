package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SoftSkill {
    COMUNICACAO("comunicacao"),
    LIDERANCA("lideranca"),
    RESOLUCAO_PROBLEMAS("resolucao_problemas"),
    TRABALHO_EQUIPE("trabalho_equipe"),
    PENSAMENTO_CRITICO("pensamento_critico"),
    ADAPTABILIDADE("adaptabilidade"),
    GESTAO_TEMPO("gestao_tempo"),
    INTELIGENCIA_EMOCIONAL("inteligencia_emocional"),
    PROATIVIDADE("proatividade"),
    CRIATIVIDADE("criatividade");

    private final String key;

    SoftSkill(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}