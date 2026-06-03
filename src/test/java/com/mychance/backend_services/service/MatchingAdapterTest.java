package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.adapter.MatchingPayload;
import com.mychance.backend_services.support.JobVacancyTestBuilder;
import com.mychance.backend_services.support.ProfileTestBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingAdapterTest {

	private final MatchingAdapter matchingAdapter = new MatchingAdapter();

	@Test
	void convertsExperienceMonthsToYears() {
		AnonymousProfile profile = ProfileTestBuilder.withExperienceMonths(14, 6);

		MatchingPayload payload = matchingAdapter.adapt(profile, JobVacancyTestBuilder.backendPythonJob());

		assertThat(payload.anosExperiencia()).isEqualTo(20 / 12.0);
	}

	@Test
	void countsProjectsForPortfolioScalar() {
		AnonymousProfile profile = ProfileTestBuilder.withProjects(
				"Projeto A",
				"Projeto B",
				"Projeto C"
		);

		MatchingPayload payload = matchingAdapter.adapt(profile, JobVacancyTestBuilder.backendPythonJob());

		assertThat(payload.projetos()).isEqualTo(3);
	}

	@Test
	void mapsCandidateSkillsAndJobWeights() {
		AnonymousProfile profile = ProfileTestBuilder.withSkills(4, 5);
		profile.addSkill(new com.mychance.backend_services.domain.entity.CandidateSkill(SkillName.DOCKER, 2));

		MatchingPayload payload = matchingAdapter.adapt(profile, JobVacancyTestBuilder.backendPythonJob());

		assertThat(payload.candidateSkills())
				.containsEntry(SkillName.PYTHON, 4)
				.containsEntry(SkillName.SQL, 5)
				.containsEntry(SkillName.DOCKER, 2);
		assertThat(payload.jobSkillActivation()).containsEntry(SkillName.PYTHON, 1);
		assertThat(payload.jobSkillWeights()).containsEntry(SkillName.PYTHON, 5);
	}
}
