package com.mychance.backend_services.dto.adapter;

import com.mychance.backend_services.domain.enums.SkillName;

import java.util.Map;

public record MatchingPayload(
		Map<SkillName, Integer> candidateSkills,
		double anosExperiencia,
		int projetos,
		Map<SkillName, Integer> jobSkillActivation,
		Map<SkillName, Integer> jobSkillWeights
) {
}
