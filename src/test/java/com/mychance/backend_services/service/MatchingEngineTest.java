package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.adapter.MatchingPayload;
import com.mychance.backend_services.support.JobVacancyTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingEngineTest {

	private final MatchingEngine matchingEngine = new MatchingEngine();

	@Test
	void returnsZeroWhenMandatorySkillIsMissing() {
		Map<SkillName, Integer> skills = Map.of(SkillName.SQL, 5);
		MatchingPayload payload = payload(skills, 2.0, 1);

		double score = matchingEngine.computeScore(payload, JobVacancyTestBuilder.pythonOnlyMandatoryJob());

		assertThat(score).isZero();
	}

	@Test
	void returnsZeroWhenMandatorySkillIsBelowMinimumLevel() {
		Map<SkillName, Integer> skills = Map.of(SkillName.PYTHON, 2);
		MatchingPayload payload = payload(skills, 2.0, 1);

		double score = matchingEngine.computeScore(payload, JobVacancyTestBuilder.pythonOnlyMandatoryJob());

		assertThat(score).isZero();
	}

	@Test
	void addsExperienceAndProjectBonuses() {
		Map<SkillName, Integer> skills = Map.of(
				SkillName.PYTHON, 5,
				SkillName.SQL, 5,
				SkillName.DOCKER, 3
		);
		MatchingPayload baseline = payload(skills, 0.0, 0);
		MatchingPayload enriched = payload(skills, 5.0, 3);

		double baselineScore = matchingEngine.computeScore(baseline, JobVacancyTestBuilder.backendPythonJob());
		double enrichedScore = matchingEngine.computeScore(enriched, JobVacancyTestBuilder.backendPythonJob());

		assertThat(enrichedScore).isGreaterThan(baselineScore);
	}

	@Test
	void capsScoreAtOne() {
		Map<SkillName, Integer> skills = Map.of(
				SkillName.PYTHON, 5,
				SkillName.SQL, 5,
				SkillName.DOCKER, 5
		);
		MatchingPayload payload = payload(skills, 10.0, 20);

		double score = matchingEngine.computeScore(payload, JobVacancyTestBuilder.backendPythonJob());

		assertThat(score).isLessThanOrEqualTo(1.0);
	}

	@Test
	void returnsZeroWhenCandidateHasNoSkills() {
		MatchingPayload payload = payload(Map.of(), 0.0, 0);

		double score = matchingEngine.computeScore(payload, JobVacancyTestBuilder.backendPythonJob());

		assertThat(score).isZero();
	}

	private MatchingPayload payload(Map<SkillName, Integer> skills, double anosExperiencia, int projetos) {
		Map<SkillName, Integer> candidateSkills = new EnumMap<>(SkillName.class);
		candidateSkills.putAll(skills);

		Map<SkillName, Integer> activation = new EnumMap<>(SkillName.class);
		Map<SkillName, Integer> weights = new EnumMap<>(SkillName.class);
		activation.put(SkillName.PYTHON, 1);
		activation.put(SkillName.SQL, 1);
		activation.put(SkillName.DOCKER, 1);
		weights.put(SkillName.PYTHON, 5);
		weights.put(SkillName.SQL, 4);
		weights.put(SkillName.DOCKER, 3);

		return new MatchingPayload(
				candidateSkills,
				anosExperiencia,
				projetos,
				activation,
				weights
		);
	}
}
