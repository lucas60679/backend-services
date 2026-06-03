package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfileCreateResponse(
		@JsonProperty("candidato_id") String candidatoId,
		@JsonProperty("message") String message
) {
}
