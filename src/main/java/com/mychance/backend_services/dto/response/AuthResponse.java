package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AuthResponse(
		@JsonProperty("token") String token,
		@JsonProperty("user_id") UUID userId,
		@JsonProperty("role") String role,
		@JsonProperty("full_name") String fullName,
		@JsonProperty("candidato_id") String candidatoId
) {
}
