package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JobRequirementDetailResponse(
		@JsonProperty("competencia") String competencia,
		@JsonProperty("peso") int peso,
		@JsonProperty("obrigatoria") boolean obrigatoria,
		@JsonProperty("nivel_min") int nivelMin
) {
}
