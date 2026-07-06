package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record ProfileCreateRequest(
		@NotEmpty @Valid @JsonProperty("competencias") Map<String, @Min(0) @Max(5) Integer> competencias,
		@NotNull @Valid @JsonProperty("experiencias") List<ExperienceRequest> experiencias,
		@NotNull @Valid @JsonProperty("projetos_destaque") List<@NotBlank @Size(max = 250) String> projetosDestaque,
		@JsonProperty("nivel_escolaridade") String nivelEscolaridade,
		@JsonProperty("regiao") String regiao,
		@NotNull @Min(1) @JsonProperty("pretensao_salarial_minima") Integer pretensaoSalarialMinima
) {
}
