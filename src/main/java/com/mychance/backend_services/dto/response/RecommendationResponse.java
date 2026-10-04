package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mychance.backend_services.domain.enums.Benefit;
import com.mychance.backend_services.domain.enums.SalaryRange;
import com.mychance.backend_services.domain.enums.SoftSkill;

import java.util.List;

public record RecommendationResponse(
		@JsonProperty("posicao") int posicao,
		@JsonProperty("candidato_id") String candidatoId,
		@JsonProperty("compatibilidade_score") double compatibilidadeScore,
		@JsonProperty("compatibilidade") String compatibilidade,
		@JsonProperty("competencias_tecnicas") List competenciasTecnicas,
		@JsonProperty("experiencias") List experiencias,
		@JsonProperty("projetos_destaque") List projetosDestaque,
		@JsonProperty("convite_status") String conviteStatus,
		@JsonProperty("faixa_salarial") SalaryRange faixaSalarial,
		@JsonProperty("soft_skills") List softSkills,
		@JsonProperty("beneficios") List beneficios
) {
}