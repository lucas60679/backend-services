package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mychance.backend_services.domain.enums.Benefit;
import com.mychance.backend_services.domain.enums.SalaryRange;
import com.mychance.backend_services.domain.enums.SoftSkill;

import java.util.List;
import java.util.Map;

public record ProfileCreateRequest(
		@JsonProperty("competencias") Map competencias,
		@JsonProperty("experiencias") List experiencias,
		@JsonProperty("projetos_destaque") List projetosDestaque,
		@JsonProperty("nivel_escolaridade") String nivelEscolaridade,
		@JsonProperty("estado") String estado,
		@JsonProperty("curso_area") String cursoArea,
		@JsonProperty("pretensao_salarial_minima") Integer pretensaoSalarialMinima,
		@JsonProperty("modalidades_preferidas") List modalidadesPreferidas,
		@JsonProperty("vinculos_preferidos") List vinculosPreferidos,
		@JsonProperty("idiomas") List idiomas,
		@JsonProperty("faixa_salarial") SalaryRange faixaSalarial,
		@JsonProperty("soft_skills") List softSkills,
		@JsonProperty("beneficios") List beneficios
) {
}