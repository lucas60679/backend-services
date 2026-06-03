package com.mychance.backend_services.support;

import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;

import java.util.UUID;

public final class JobVacancyTestBuilder {

	private JobVacancyTestBuilder() {
	}

	public static JobVacancy backendPythonJob() {
		JobVacancy vacancy = new JobVacancy(
				"Desenvolvedor Backend Python",
				UUID.fromString("11111111-1111-1111-1111-111111111111")
		);
		vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.SQL, 4, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.DOCKER, 3, false, 2));
		return vacancy;
	}

	public static JobVacancy pythonOnlyMandatoryJob() {
		JobVacancy vacancy = new JobVacancy("Python Specialist", UUID.randomUUID());
		vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 4));
		return vacancy;
	}
}
