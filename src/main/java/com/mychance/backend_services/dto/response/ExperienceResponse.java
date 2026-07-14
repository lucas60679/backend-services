package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExperienceResponse(
		@JsonProperty("cargo") String cargo,
		@JsonProperty("senioridade") String senioridade,
		@JsonProperty("inicio_mes") int inicioMes,
		@JsonProperty("inicio_ano") int inicioAno,
		@JsonProperty("fim_mes") Integer fimMes,
		@JsonProperty("fim_ano") Integer fimAno,
		@JsonProperty("atual") boolean atual,
		@JsonProperty("tempo_meses") int tempoMeses
) {
}
