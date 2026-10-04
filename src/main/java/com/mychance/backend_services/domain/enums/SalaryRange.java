package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SalaryRange {
    ATE_2K("ate_2k", 1),
    DE_2K_A_4K("2k_a_4k", 2),
    DE_4K_A_7K("4k_a_7k", 3),
    DE_7K_A_10K("7k_a_10k", 4),
    DE_10K_A_15K("10k_a_15k", 5),
    ACIMA_15K("acima_15k", 6),
    A_COMBINAR("a_combinar", 0);

    private final String key;
    private final int rank;

    SalaryRange(String key, int rank) {
        this.key = key;
        this.rank = rank;
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    public int getRank() {
        return rank;
    }
}
