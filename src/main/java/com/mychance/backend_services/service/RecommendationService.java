package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.nlp.MatchingRankRequest;
import com.mychance.backend_services.dto.nlp.MatchingRankResponse;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final MatchingRequestBuilder matchingRequestBuilder;
	private final NlpMatchingClient nlpMatchingClient;

	public RecommendationService(
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			MatchingRequestBuilder matchingRequestBuilder,
			NlpMatchingClient nlpMatchingClient
	) {
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.matchingRequestBuilder = matchingRequestBuilder;
		this.nlpMatchingClient = nlpMatchingClient;
	}

	@Transactional(readOnly = true)
	public List<RecommendationResponse> getRecommendations(UUID jobId) {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdWithRequirements(jobId)
				.orElseThrow(() -> new JobNotFoundException(jobId));

		List<AnonymousProfile> profiles = anonymousProfileRepository.findAll().stream()
				.filter(profile -> isSalaryCompatible(profile, jobVacancy))
				.toList();
		profiles.forEach(this::initializeProfileDetails);

		if (profiles.isEmpty()) {
			return List.of();
		}

		MatchingRankRequest request = matchingRequestBuilder.buildBatchRequest(profiles, jobVacancy);
		MatchingRankResponse nlpResponse = nlpMatchingClient.rankCandidates(request);

		Map<String, AnonymousProfile> profilesByPublicId = profiles.stream()
				.collect(Collectors.toMap(
						profile -> PublicIdFormatter.toPublicCandidateId(profile.getId()),
						Function.identity()
				));

		return nlpResponse.ranking().stream()
				.filter(ranking -> ranking.aprovadoFiltragem() && ranking.compatibilidadeScore() > 0.0)
				.map(ranking -> toRecommendation(ranking, profilesByPublicId.get(ranking.candidatoId())))
				.filter(recommendation -> recommendation != null)
				.sorted(Comparator.comparingDouble(RecommendationResponse::compatibilidadeScore).reversed())
				.toList();
	}

	private boolean isSalaryCompatible(AnonymousProfile profile, JobVacancy jobVacancy) {
		if (jobVacancy.getMaxSalary() == null || profile.getSalaryExpectationMin() == null) {
			return true;
		}
		return profile.getSalaryExpectationMin() <= jobVacancy.getMaxSalary();
	}

	private void initializeProfileDetails(AnonymousProfile profile) {
		profile.getSkills().size();
		profile.getProjects().size();
		profile.getExperiences().size();
	}

	private RecommendationResponse toRecommendation(
			MatchingRankResponse.RankedCandidatePayload ranking,
			AnonymousProfile profile
	) {
		if (profile == null) {
			return null;
		}

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
				ranking.candidatoId(),
				Math.round(ranking.compatibilidadeScore() * 100.0) / 100.0,
				ranking.compatibilidade(),
				competencias,
				experiencias,
				projetos
		);
	}
}
