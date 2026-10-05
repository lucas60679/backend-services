package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mychance.backend_services.domain.enums.Benefit;
import com.mychance.backend_services.domain.enums.SalaryRange;
import com.mychance.backend_services.domain.enums.SoftSkill;
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
		@NotEmpty @Valid @JsonProperty("competencias") Map competencias,
		@NotNull @Valid @JsonProperty("experiencias") List experiencias,
		@NotNull @Valid @JsonProperty("projetos_destaque") List<@NotBlank @Size(max = 250) String> projetosDestaque,
		@JsonProperty("nivel_escolaridade") String nivelEscolaridade,
		@JsonProperty("estado") String estado,
		@Size(max = 150) @JsonProperty("curso_area") String cursoArea,
		@Min(1000) @JsonProperty("pretensao_salarial_minima") Integer pretensaoSalarialMinima,
		@NotEmpty @JsonProperty("modalidades_preferidas") List<@NotBlank String> modalidadesPreferidas,
		@NotEmpty @JsonProperty("vinculos_preferidos") List<@NotBlank String> vinculosPreferidos,
		@NotNull @Valid @JsonProperty("idiomas") List idiomas,
		@NotNull @JsonProperty("faixa_salarial") SalaryRange faixaSalarial,
		@NotNull @JsonProperty("soft_skills") List softSkills,
		@NotNull @JsonProperty("beneficios") List beneficios
) {
}