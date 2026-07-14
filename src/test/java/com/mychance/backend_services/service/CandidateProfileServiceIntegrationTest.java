package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.dto.response.RecommendationResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestNlpMatchingConfig.class)
@Transactional
class CandidateProfileServiceIntegrationTest {

	@Autowired
	private CandidateProfileService candidateProfileService;

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void createProfileAndFetchRecommendations() {
		Account account = accountRepository.save(AccountTestBuilder.candidateAccount("candidate-integration@test.local"));

		ProfileCreateRequest request = ProfileRequestTestBuilder.basicProfile(
				Map.of("python", 4, "sql", 5, "docker", 2, "powerbi", 0),
				List.of(
						ProfileRequestTestBuilder.currentExperience("Desenvolvedor Backend Júnior", "junior", 14),
						ProfileRequestTestBuilder.experience("Pesquisador", "estagio", 1, 2022, 6, 2022, false)
				)
		);

		ProfileCreateResponse created = candidateProfileService.createProfile(account.getId(), request);
		assertThat(created.candidatoId()).startsWith("usr_");

		JobVacancy job = jobVacancyRepository.findAll().get(0);
		List<RecommendationResponse> recommendations = recommendationService.getRecommendations(job.getId());

		assertThat(recommendations).isNotEmpty();
		assertThat(recommendations.get(0).compatibilidadeScore()).isGreaterThan(0.0);
		assertThat(recommendations.get(0).compatibilidade()).isNotBlank();
		assertThat(recommendations.get(0).competenciasTecnicas()).contains("Python", "SQL");
		assertThat(recommendations.get(0).candidatoId()).matches("usr_[a-f0-9]{8}");
		assertThat(recommendations.get(0).experiencias()).hasSize(2);
		assertThat(recommendations.get(0).projetosDestaque()).hasSize(1);
	}
}
