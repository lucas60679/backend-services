package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record JobCreateRequest(
		@NotBlank @JsonProperty("titulo") String titulo,
		@NotNull @JsonProperty("recrutador_id") UUID recrutadorId,
		@Size(max = 2000) @JsonProperty("descricao") String descricao,
		@NotNull @Min(1) @JsonProperty("salario_maximo") Integer salarioMaximo,
		@NotEmpty @Valid @JsonProperty("requisitos") List<JobRequirementRequest> requisitos
) {
}
