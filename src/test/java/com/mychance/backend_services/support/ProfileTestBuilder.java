package com.mychance.backend_services.support;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.Candidate;
import com.mychance.backend_services.domain.entity.CandidateExperience;
import com.mychance.backend_services.domain.entity.CandidateProject;
import com.mychance.backend_services.domain.entity.CandidateSkill;
import com.mychance.backend_services.domain.enums.SeniorityLevel;
import com.mychance.backend_services.domain.enums.SkillName;

public final class ProfileTestBuilder {

	private ProfileTestBuilder() {
	}

	public static AnonymousProfile anonymousProfile() {
		Account account = AccountTestBuilder.candidateAccount("teste@mychance.local");
		Candidate candidate = AccountTestBuilder.candidateFrom(account);
		return new AnonymousProfile(candidate);
	}

	public static AnonymousProfile withSkills(int pythonLevel, int sqlLevel) {
		AnonymousProfile profile = anonymousProfile();
		profile.addSkill(new CandidateSkill(SkillName.PYTHON, pythonLevel));
		profile.addSkill(new CandidateSkill(SkillName.SQL, sqlLevel));
		return profile;
	}

	public static AnonymousProfile withExperienceMonths(int... months) {
		AnonymousProfile profile = anonymousProfile();
		java.time.YearMonth end = java.time.YearMonth.now();
		for (int monthCount : months) {
			java.time.YearMonth start = end.minusMonths(Math.max(1, monthCount) - 1L);
			profile.addExperience(new CandidateExperience(
					"Cargo Teste",
					SeniorityLevel.PLENO,
					start.getMonthValue(),
					start.getYear(),
					end.getMonthValue(),
					end.getYear(),
					false
			));
		}
		return profile;
	}

	public static AnonymousProfile withProjects(String... descriptions) {
		AnonymousProfile profile = anonymousProfile();
		for (String description : descriptions) {
			profile.addProject(new CandidateProject(description));
		}
		return profile;
	}
}
