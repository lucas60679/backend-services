package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		@NotBlank @Size(max = 255) @JsonProperty("nome") String nome,
		@NotBlank @Email @JsonProperty("email") String email,
		@NotBlank @Size(min = 6, max = 100) @JsonProperty("senha") String senha,
		@NotNull @JsonProperty("role") String role
) {
}
