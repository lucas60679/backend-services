package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.support.AccountTestBuilder;
import com.mychance.backend_services.support.ProfileRequestTestBuilder;
import com.mychance.backend_services.support.TestNlpMatchingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestNlpMatchingConfig.class)
@Transactional
class RecommendationServiceIntegrationTest {

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private CandidateProfileService candidateProfileService;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewInviteService interviewInviteService;

	private int profileCounter;

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

	@Test
	void includesRejectedCandidatesWithInviteStatusFlag() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		Account account = accountRepository.save(
				AccountTestBuilder.candidateAccount("recommendation-rejected@test.local")
		);
		ProfileCreateResponse profile = candidateProfileService.createProfile(
				account.getId(),
				ProfileRequestTestBuilder.basicProfile(
						Map.of("python", 5, "sql", 5),
						List.of(ProfileRequestTestBuilder.currentExperience("Desenvolvedor", "pleno", 24))
				)
		);

		InviteResponse sent = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Proposta inicial")
		);
		interviewInviteService.rejectInvite(sent.conviteId(), account.getId());

		List<RecommendationResponse> recommendations = recommendationService.getRecommendations(job.getId());

		assertThat(recommendations)
				.extracting(RecommendationResponse::candidatoId)
				.contains(profile.candidatoId());

		RecommendationResponse rejected = recommendations.stream()
				.filter(recommendation -> recommendation.candidatoId().equals(profile.candidatoId()))
				.findFirst()
				.orElseThrow();

		assertThat(rejected.conviteStatus()).isEqualTo("RECUSADO");
	}

	private void createProfile(Map<String, Integer> skills, int experienceMonths) {
		profileCounter++;
		Account account = accountRepository.save(
				AccountTestBuilder.candidateAccount("recommendation-" + profileCounter + "@test.local")
		);
		candidateProfileService.createProfile(
				account.getId(),
				ProfileRequestTestBuilder.basicProfile(
						skills,
						List.of(ProfileRequestTestBuilder.currentExperience("Desenvolvedor", "pleno", experienceMonths))
				)
		);
	}
}
