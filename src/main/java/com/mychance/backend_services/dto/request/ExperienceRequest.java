package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExperienceRequest(
		@NotBlank @JsonProperty("cargo") String cargo,
		@NotNull @Min(1) @JsonProperty("tempo_meses") Integer tempoMeses
) {
}
