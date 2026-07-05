package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.request.ExperienceRequest;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RecommendationServiceIntegrationTest {

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private CandidateProfileService candidateProfileService;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Test
	void throwsWhenJobDoesNotExist() {
		UUID missingJobId = UUID.fromString("00000000-0000-0000-0000-000000000099");

		assertThatThrownBy(() -> recommendationService.getRecommendations(missingJobId))
				.isInstanceOf(JobNotFoundException.class);
	}

	@Test
	void excludesCandidatesWhoFailMandatoryRequirements() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);

		createProfile(Map.of("python", 1, "sql", 5), 12);
		createProfile(Map.of("python", 5, "sql", 5), 12);

		List<RecommendationResponse> recommendations = recommendationService.getRecommendations(job.getId());

		assertThat(recommendations).hasSize(1);
		assertThat(recommendations.get(0).compatibilidadeScore()).isGreaterThan(0.0);
	}

	@Test
	void ranksStrongerCandidatesFirst() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);

		createProfile(Map.of("python", 3, "sql", 3, "docker", 1), 6);
		createProfile(Map.of("python", 5, "sql", 5, "docker", 4), 24);

		List<RecommendationResponse> recommendations = recommendationService.getRecommendations(job.getId());

		assertThat(recommendations).hasSizeGreaterThanOrEqualTo(2);
		assertThat(recommendations.get(0).compatibilidadeScore())
				.isGreaterThanOrEqualTo(recommendations.get(1).compatibilidadeScore());
	}

	@Test
	void returnsAnonymousPayloadWithoutPii() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		createProfile(Map.of("python", 5, "sql", 5), 12);

		RecommendationResponse recommendation = recommendationService.getRecommendations(job.getId()).get(0);

		assertThat(recommendation.candidatoId()).startsWith("usr_");
		assertThat(recommendation.competenciasTecnicas()).isNotEmpty();
		assertThat(recommendation.experiencias()).isNotEmpty();
		assertThat(recommendation.projetosDestaque()).isNotEmpty();
	}

	@Test
	void omitsZeroLevelSkillsFromTechnicalCompetencies() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		createProfile(Map.of("python", 5, "sql", 5, "powerbi", 0), 12);

		RecommendationResponse recommendation = recommendationService.getRecommendations(job.getId()).get(0);

		assertThat(recommendation.competenciasTecnicas())
				.contains("Python", "SQL")
				.doesNotContain("Power BI");
	}

	private void createProfile(Map<String, Integer> skills, int experienceMonths) {
		candidateProfileService.createProfile(new ProfileCreateRequest(
				skills,
				List.of(new ExperienceRequest("Desenvolvedor", experienceMonths)),
				List.of("Projeto relevante para a vaga.")
		));
	}
}
