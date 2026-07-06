package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record JobRequirementRequest(
		@NotBlank @JsonProperty("competencia") String competencia,
		@Min(1) @Max(5) @JsonProperty("peso") int peso,
		@JsonProperty("obrigatoria") boolean obrigatoria,
		@Min(1) @Max(5) @JsonProperty("nivel_min") int nivelMin
) {
}
