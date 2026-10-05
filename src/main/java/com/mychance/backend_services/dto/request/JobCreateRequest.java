package com.mychance.backend_services.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mychance.backend_services.domain.enums.Benefit;
import com.mychance.backend_services.domain.enums.SalaryRange;
import com.mychance.backend_services.domain.enums.SoftSkill;

import java.util.List;

public record JobCreateRequest(
		@JsonProperty("titulo") String titulo,
		@JsonProperty("descricao") String descricao,
		@JsonProperty("empresa_instituicao") String empresaInstituicao,
		@JsonProperty("modalidade") String modalidade,
		@JsonProperty("tipo_vinculo") String tipoVinculo,
		@JsonProperty("local") String local,
		@JsonProperty("senioridade") String senioridade,
		@JsonProperty("faixa_salarial") SalaryRange faixaSalarial,
		@JsonProperty("soft_skills") List softSkills,
		@JsonProperty("beneficios") List beneficios,
		@JsonProperty("requisitos") List requisitos,
		@JsonProperty("idiomas") List idiomas
) {
}