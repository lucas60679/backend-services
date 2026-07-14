package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record JobLanguageRequirementRequest(
		@NotBlank @JsonProperty("idioma") String idioma,
		@NotBlank @JsonProperty("nivel_min") String nivelMin
) {
}
