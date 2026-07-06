package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record JobCreateResponse(
		@JsonProperty("vaga_id") UUID vagaId,
		@JsonProperty("message") String message
) {
}
