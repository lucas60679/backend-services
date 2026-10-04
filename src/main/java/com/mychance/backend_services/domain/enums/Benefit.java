package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Benefit {
    PLANO_SAUDE("plano_saude"),
    PLANO_ODONTOLOGICO("plano_odontologico"),
    VALE_ALIMENTACAO("vale_alimentacao"),
    VALE_REFEICAO("vale_refeicao"),
    AUXILIO_HOME_OFFICE("auxilio_home_office"),
    GYMPASS("gympass"),
    PLR("plr"),
    SEGURO_VIDA("seguro_vida"),
    HORARIO_FLEXIVEL("horario_flexivel"),
    DAY_OFF_ANIVERSARIO("day_off_aniversario");

    private final String key;

    Benefit(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}