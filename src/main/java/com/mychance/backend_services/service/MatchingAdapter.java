package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.adapter.MatchingPayload;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class MatchingAdapter {

	public MatchingPayload adapt(AnonymousProfile profile, JobVacancy jobVacancy) {
		Map<SkillName, Integer> candidateSkills = new EnumMap<>(SkillName.class);
		profile.getSkills().forEach(skill ->
				candidateSkills.put(skill.getSkillName(), skill.getSkillLevel())
		);

		int totalMonths = profile.getExperiences().stream()
				.mapToInt(experience -> experience.getDurationMonths())
				.sum();
		double anosExperiencia = totalMonths / 12.0;
		int projetos = profile.getProjects().size();

		Map<SkillName, Integer> jobSkillActivation = new EnumMap<>(SkillName.class);
		Map<SkillName, Integer> jobSkillWeights = new EnumMap<>(SkillName.class);

		for (JobRequirement requirement : jobVacancy.getRequirements()) {
			jobSkillActivation.put(requirement.getSkillName(), 1);
			jobSkillWeights.put(requirement.getSkillName(), requirement.getWeight());
		}

		return new MatchingPayload(
				candidateSkills,
				anosExperiencia,
				projetos,
				jobSkillActivation,
				jobSkillWeights
		);
	}
}
