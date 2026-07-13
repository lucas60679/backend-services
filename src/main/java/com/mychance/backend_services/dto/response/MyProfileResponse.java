package com.mychance.backend_services.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record MyProfileResponse(
		@JsonProperty("candidato_id") String candidatoId,
		@JsonProperty("competencias") Map<String, Integer> competencias,
		@JsonProperty("experiencias") List<ExperienceResponse> experiencias,
		@JsonProperty("projetos_destaque") List<String> projetosDestaque,
		@JsonProperty("nivel_escolaridade") String nivelEscolaridade,
		@JsonProperty("regiao") String regiao,
		@JsonProperty("pretensao_salarial_minima") Integer pretensaoSalarialMinima
) {
}
