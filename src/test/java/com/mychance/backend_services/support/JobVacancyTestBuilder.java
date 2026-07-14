package com.mychance.backend_services.support;

import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.EmploymentType;
import com.mychance.backend_services.domain.enums.SeniorityLevel;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.domain.enums.WorkModality;

import java.util.UUID;

public final class JobVacancyTestBuilder {

	private JobVacancyTestBuilder() {
	}

	public static JobVacancy backendPythonJob() {
		JobVacancy vacancy = new JobVacancy(
				"Desenvolvedor Backend Python",
				UUID.fromString("11111111-1111-1111-1111-111111111111"),
				"Vaga backend Python",
				"MyChance Labs",
				WorkModality.REMOTO,
				EmploymentType.CLT,
				null,
				SeniorityLevel.PLENO,
				8000,
				12000
		);
		vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.SQL, 4, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.DOCKER, 3, false, 2));
		return vacancy;
	}

	public static JobVacancy pythonOnlyMandatoryJob() {
		JobVacancy vacancy = new JobVacancy(
				"Python Specialist",
				UUID.randomUUID(),
				"Especialista Python",
				"Empresa Teste",
				WorkModality.REMOTO,
				EmploymentType.CLT,
				null,
				SeniorityLevel.SENIOR,
				null,
				15000
		);
		vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 4));
		return vacancy;
	}
}
