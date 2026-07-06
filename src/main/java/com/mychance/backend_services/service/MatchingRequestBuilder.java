package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.nlp.MatchingRankRequest;
import com.mychance.backend_services.dto.nlp.MatchingRankRequest.CandidateMatchingPayload;
import com.mychance.backend_services.dto.nlp.MatchingRankRequest.JobMatchingPayload;
import com.mychance.backend_services.dto.nlp.MatchingRankRequest.JobRequirementPayload;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MatchingRequestBuilder {

	public List<String> globalVocabulary() {
		return Arrays.stream(SkillName.values())
				.map(SkillName::getKey)
				.toList();
	}

	public MatchingRankRequest buildBatchRequest(List<AnonymousProfile> profiles, JobVacancy jobVacancy) {
		Map<String, JobRequirementPayload> competencias = new LinkedHashMap<>();
		jobVacancy.getRequirements().forEach(requirement -> competencias.put(
				requirement.getSkillName().getKey(),
				new JobRequirementPayload(
						requirement.getWeight(),
						requirement.isMandatory(),
						requirement.getMinLevel()
				)
		));

		List<CandidateMatchingPayload> candidatos = profiles.stream()
				.map(profile -> new CandidateMatchingPayload(
						PublicIdFormatter.toPublicCandidateId(profile.getId()),
						buildCandidateSkills(profile),
						profile.getProjects().size(),
						totalExperienceYears(profile)
				))
				.toList();

		return new MatchingRankRequest(
				globalVocabulary(),
				new JobMatchingPayload(competencias),
				candidatos
		);
	}

	private Map<String, Integer> buildCandidateSkills(AnonymousProfile profile) {
		Map<String, Integer> skills = new LinkedHashMap<>();
		profile.getSkills().forEach(skill ->
				skills.put(skill.getSkillName().getKey(), skill.getSkillLevel())
		);
		return skills;
	}

	private double totalExperienceYears(AnonymousProfile profile) {
		int totalMonths = profile.getExperiences().stream()
				.mapToInt(experience -> experience.getDurationMonths())
				.sum();
		return totalMonths / 12.0;
	}
}
