package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExperienceResponse(
		@JsonProperty("cargo") String cargo,
		@JsonProperty("tempo_meses") int tempoMeses
) {
}
