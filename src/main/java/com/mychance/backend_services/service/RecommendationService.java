package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.adapter.MatchingPayload;
import com.mychance.backend_services.dto.response.ExperienceResponse;
import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final MatchingAdapter matchingAdapter;
	private final MatchingEngine matchingEngine;

	public RecommendationService(
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			MatchingAdapter matchingAdapter,
			MatchingEngine matchingEngine
	) {
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.matchingAdapter = matchingAdapter;
		this.matchingEngine = matchingEngine;
	}

	@Transactional(readOnly = true)
	public List<RecommendationResponse> getRecommendations(UUID jobId) {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdWithRequirements(jobId)
				.orElseThrow(() -> new JobNotFoundException(jobId));

		List<AnonymousProfile> profiles = anonymousProfileRepository.findAll();
		profiles.forEach(this::initializeProfileDetails);

		return profiles.stream()
				.map(profile -> toRecommendation(profile, jobVacancy))
				.filter(recommendation -> recommendation.compatibilidadeScore() > 0.0)
				.sorted(Comparator.comparingDouble(RecommendationResponse::compatibilidadeScore).reversed())
				.toList();
	}

	private void initializeProfileDetails(AnonymousProfile profile) {
		profile.getSkills().size();
		profile.getProjects().size();
		profile.getExperiences().size();
	}

	private RecommendationResponse toRecommendation(AnonymousProfile profile, JobVacancy jobVacancy) {
		MatchingPayload payload = matchingAdapter.adapt(profile, jobVacancy);
		double score = matchingEngine.computeScore(payload, jobVacancy);

		List<String> competencias = profile.getSkills().stream()
				.filter(skill -> skill.getSkillLevel() > 0)
				.map(skill -> skill.getSkillName().getDisplayName())
				.toList();

		List<ExperienceResponse> experiencias = profile.getExperiences().stream()
				.map(experience -> new ExperienceResponse(experience.getRoleTitle(), experience.getDurationMonths()))
				.toList();

		List<String> projetos = profile.getProjects().stream()
				.map(project -> project.getDescription())
				.toList();

		return new RecommendationResponse(
				PublicIdFormatter.toPublicCandidateId(profile.getId()),
				Math.round(score * 100.0) / 100.0,
				competencias,
				experiencias,
				projetos
		);
	}
}
