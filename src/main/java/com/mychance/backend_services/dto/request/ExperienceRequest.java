package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExperienceRequest(
		@NotBlank @JsonProperty("cargo") String cargo,
		@NotBlank @JsonProperty("senioridade") String senioridade,
		@NotNull @Min(1) @Max(12) @JsonProperty("inicio_mes") Integer inicioMes,
		@NotNull @Min(1950) @Max(2100) @JsonProperty("inicio_ano") Integer inicioAno,
		@Min(1) @Max(12) @JsonProperty("fim_mes") Integer fimMes,
		@Min(1950) @Max(2100) @JsonProperty("fim_ano") Integer fimAno,
		@JsonProperty("atual") Boolean atual
) {
}
