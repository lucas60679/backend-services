package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.request.JobCreateRequest;
import com.mychance.backend_services.dto.request.JobRequirementRequest;
import com.mychance.backend_services.dto.response.JobCreateResponse;
import com.mychance.backend_services.dto.response.JobSummaryResponse;
import com.mychance.backend_services.exception.InvalidSkillException;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.exception.ResourceAccessDeniedException;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class JobVacancyService {

	private final JobVacancyRepository jobVacancyRepository;

	public JobVacancyService(JobVacancyRepository jobVacancyRepository) {
		this.jobVacancyRepository = jobVacancyRepository;
	}

	@Transactional
	public JobCreateResponse createJob(UUID recruiterId, JobCreateRequest request) {
		JobVacancy jobVacancy = new JobVacancy(
				request.titulo(),
				recruiterId,
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

	@Transactional(readOnly = true)
	public JobVacancy getOwnedJob(UUID recruiterId, UUID jobId) {
		JobVacancy jobVacancy = getJob(jobId);
		if (!jobVacancy.getRecruiterId().equals(recruiterId)) {
			throw new ResourceAccessDeniedException("Recruiter does not own this job");
		}
		return jobVacancy;
	}

	@Transactional(readOnly = true)
	public List<JobSummaryResponse> listRecruiterJobs(UUID recruiterId) {
		return jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(recruiterId).stream()
				.map(job -> new JobSummaryResponse(job.getId(), job.getTitle()))
				.toList();
	}
}
