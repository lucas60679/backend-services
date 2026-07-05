package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.dto.request.ExperienceRequest;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.exception.InvalidSkillException;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CandidateProfileServiceTest {

	@Autowired
	private CandidateProfileService candidateProfileService;

	@Autowired
	private AnonymousProfileRepository anonymousProfileRepository;

	@Test
	void rejectsUnknownSkills() {
		ProfileCreateRequest request = new ProfileCreateRequest(
				Map.of("kubernetes", 5),
				List.of(new ExperienceRequest("Dev", 12)),
				List.of("Projeto limpo")
		);

		assertThatThrownBy(() -> candidateProfileService.createProfile(request))
				.isInstanceOf(InvalidSkillException.class)
				.hasMessageContaining("kubernetes");
	}

	@Test
	void sanitizesProjectDescriptionsBeforePersisting() {
		ProfileCreateRequest request = new ProfileCreateRequest(
				Map.of("python", 3),
				List.of(new ExperienceRequest("Dev", 12)),
				List.of("Contato: dev@email.com — veja github.com/dev/portfolio")
		);

		candidateProfileService.createProfile(request);

		AnonymousProfile profile = anonymousProfileRepository.findAll().get(0);
		String savedDescription = profile.getProjects().get(0).getDescription();

		assertThat(savedDescription).contains("[email removido]");
		assertThat(savedDescription).contains("[link removido]");
		assertThat(savedDescription).doesNotContain("dev@email.com");
	}

	@Test
	void persistsSkillsExperiencesAndProjects() {
		ProfileCreateRequest request = new ProfileCreateRequest(
				Map.of("python", 4, "sql", 5),
				List.of(
						new ExperienceRequest("Backend Júnior", 14),
						new ExperienceRequest("Estagiário", 6)
				),
				List.of("API REST", "Pipeline de dados")
		);

		candidateProfileService.createProfile(request);

		AnonymousProfile profile = anonymousProfileRepository.findAll().get(0);

		assertThat(profile.getSkills()).hasSize(2);
		assertThat(profile.getExperiences()).hasSize(2);
		assertThat(profile.getProjects()).hasSize(2);
		assertThat(profile.getCandidate().getFullName()).startsWith("Candidato ");
	}
}
