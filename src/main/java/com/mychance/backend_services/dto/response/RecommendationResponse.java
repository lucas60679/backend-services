package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecommendationResponse(
		@JsonProperty("posicao") int posicao,
		@JsonProperty("candidato_id") String candidatoId,
		@JsonProperty("compatibilidade_score") double compatibilidadeScore,
		@JsonProperty("compatibilidade") String compatibilidade,
		@JsonProperty("competencias_tecnicas") List<String> competenciasTecnicas,
		@JsonProperty("experiencias") List<ExperienceResponse> experiencias,
		@JsonProperty("projetos_destaque") List<String> projetosDestaque
) {
}
