package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		@NotBlank @Email @JsonProperty("email") String email,
		@NotBlank @Size(min = 6, max = 100) @JsonProperty("senha") String senha
) {
}
