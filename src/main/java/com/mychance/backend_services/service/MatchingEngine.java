package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.adapter.MatchingPayload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MatchingEngine {

	private static final double PROJECT_BONUS_FACTOR = 0.02;
	private static final double EXPERIENCE_BONUS_FACTOR = 0.03;
	private static final double MAX_EXPERIENCE_YEARS = 10.0;

	public double computeScore(MatchingPayload payload, JobVacancy jobVacancy) {
		for (JobRequirement requirement : jobVacancy.getRequirements()) {
			if (requirement.isMandatory()) {
				Integer level = payload.candidateSkills().get(requirement.getSkillName());
				if (level == null || level < requirement.getMinLevel()) {
					return 0.0;
				}
			}
		}

		double cosine = weightedCosineSimilarity(
				payload.candidateSkills(),
				payload.jobSkillActivation(),
				payload.jobSkillWeights()
		);

		double experienceBonus = Math.min(payload.anosExperiencia(), MAX_EXPERIENCE_YEARS)
				* EXPERIENCE_BONUS_FACTOR;
		double projectBonus = payload.projetos() * PROJECT_BONUS_FACTOR;

		return Math.min(1.0, cosine + experienceBonus + projectBonus);
	}

	private double weightedCosineSimilarity(
			Map<SkillName, Integer> candidateSkills,
			Map<SkillName, Integer> jobActivation,
			Map<SkillName, Integer> weights
	) {
		double dotProduct = 0.0;
		double candidateMagnitude = 0.0;
		double jobMagnitude = 0.0;

		for (SkillName skill : SkillName.values()) {
			double candidateValue = candidateSkills.getOrDefault(skill, 0);
			double jobValue = jobActivation.getOrDefault(skill, 0) == 0
					? 0.0
					: weights.getOrDefault(skill, 1);

			dotProduct += candidateValue * jobValue;
			candidateMagnitude += candidateValue * candidateValue;
			jobMagnitude += jobValue * jobValue;
		}

		if (candidateMagnitude == 0.0 || jobMagnitude == 0.0) {
			return 0.0;
		}

		return dotProduct / (Math.sqrt(candidateMagnitude) * Math.sqrt(jobMagnitude));
	}
}
