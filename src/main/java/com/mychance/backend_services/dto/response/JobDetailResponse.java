package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record JobDetailResponse(
		@JsonProperty("vaga_id") UUID vagaId,
		@JsonProperty("titulo") String titulo,
		@JsonProperty("descricao") String descricao,
		@JsonProperty("salario_maximo") Integer salarioMaximo,
		@JsonProperty("requisitos") List<JobRequirementDetailResponse> requisitos
) {
}
