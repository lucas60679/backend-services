package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.dto.request.ExperienceRequest;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.repository.JobVacancyRepository;
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

	@Test
	void createProfileAndFetchRecommendations() {
		ProfileCreateRequest request = new ProfileCreateRequest(
				Map.of("python", 4, "sql", 5, "docker", 2, "powerbi", 0),
				List.of(
						new ExperienceRequest("Desenvolvedor Backend Júnior", 14),
						new ExperienceRequest("Estagiário de Dados", 6)
				),
				List.of("Desenvolvimento de API de e-commerce utilizando Python e Docker."),
				null,
				null,
				7000
		);

		ProfileCreateResponse created = candidateProfileService.createProfile(request);
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
