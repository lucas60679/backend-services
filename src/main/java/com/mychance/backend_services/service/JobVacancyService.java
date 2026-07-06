package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.request.JobCreateRequest;
import com.mychance.backend_services.dto.request.JobRequirementRequest;
import com.mychance.backend_services.dto.response.JobCreateResponse;
import com.mychance.backend_services.exception.InvalidSkillException;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class JobVacancyService {

	private final JobVacancyRepository jobVacancyRepository;

	public JobVacancyService(JobVacancyRepository jobVacancyRepository) {
		this.jobVacancyRepository = jobVacancyRepository;
	}

	@Transactional
	public JobCreateResponse createJob(JobCreateRequest request) {
		JobVacancy jobVacancy = new JobVacancy(
				request.titulo(),
				request.recrutadorId(),
				request.descricao(),
				request.salarioMaximo()
		);
		for (JobRequirementRequest requirement : request.requisitos()) {
			if (!SkillName.isValidKey(requirement.competencia())) {
				throw new InvalidSkillException(requirement.competencia());
			}
			jobVacancy.addRequirement(new JobRequirement(
					SkillName.fromKey(requirement.competencia()),
					requirement.peso(),
					requirement.obrigatoria(),
					requirement.nivelMin()
			));
		}

		JobVacancy saved = jobVacancyRepository.save(jobVacancy);
		return new JobCreateResponse(saved.getId(), "Vaga criada com sucesso");
	}

	@Transactional(readOnly = true)
	public JobVacancy getJob(UUID jobId) {
		return jobVacancyRepository.findByIdWithRequirements(jobId)
				.orElseThrow(() -> new JobNotFoundException(jobId));
	}
}
