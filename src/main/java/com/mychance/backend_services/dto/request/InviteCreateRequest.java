package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InviteCreateRequest(
		@NotBlank @JsonProperty("candidato_id") String candidatoId,
		@Size(max = 500) @JsonProperty("mensagem") String mensagem
) {
}
