package com.mychance.backend_services.config;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.EmploymentType;
import com.mychance.backend_services.domain.enums.SeniorityLevel;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.domain.enums.WorkModality;
import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.request.ScheduleInterviewRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.exception.ProfileNotFoundException;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.service.InterviewInviteService;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class TestScenarioSeeder {

	private static final Logger log = LoggerFactory.getLogger(TestScenarioSeeder.class);

	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final InterviewInviteService interviewInviteService;

	public TestScenarioSeeder(
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			InterviewInviteService interviewInviteService
	) {
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.interviewInviteService = interviewInviteService;
	}

	public void seedScenario(TestScenario scenario) {
		if (scenario == TestScenario.BASE) {
			return;
		}
		if (scenario == TestScenario.R1) {
			seedRecruiterScenarioR1();
			return;
		}
		if (scenario == TestScenario.C2) {
			seedCandidateScenarioC2();
		}
	}

	private void seedRecruiterScenarioR1() {
		JobVacancy backendJob = findBackendJob();
		String anaPublicId = publicIdFor(DemoDataSeeder.ANA_ID);
		String diegoPublicId = publicIdFor(DemoDataSeeder.DIEGO_ID);
		String elisaPublicId = publicIdFor(DemoDataSeeder.ELISA_ID);

		InviteResponse anaInvite = interviewInviteService.sendInvite(
				backendJob.getId(),
				new InviteCreateRequest(anaPublicId, "Gostaríamos de conversar sobre a vaga de Backend Python.")
		);
		interviewInviteService.acceptInvite(anaInvite.conviteId(), DemoDataSeeder.ANA_ID);
		interviewInviteService.scheduleInterview(
				anaInvite.conviteId(),
				DemoDataSeeder.RECRUITER_ALPHA_ID,
				new ScheduleInterviewRequest(defaultInterviewProposal(), null)
		);

		interviewInviteService.sendInvite(
				backendJob.getId(),
				new InviteCreateRequest(diegoPublicId, "Seu perfil chamou nossa atenção para a vaga de Backend Python.")
		);

		InviteResponse elisaInvite = interviewInviteService.sendInvite(
				backendJob.getId(),
				new InviteCreateRequest(elisaPublicId, "Convite para entrevista na vaga de Backend Python.")
		);
		interviewInviteService.rejectInvite(elisaInvite.conviteId(), DemoDataSeeder.ELISA_ID);

		log.info("Test scenario R1 ready for recruiter@mychance.local");
	}

	private void seedCandidateScenarioC2() {
		JobVacancy backendJob = findBackendJob();
		JobVacancy frontendJob = findFrontendJob();
		JobVacancy internshipJob = seedInternshipJobIfMissing();
		String anaPublicId = publicIdFor(DemoDataSeeder.ANA_ID);

		InviteResponse backendInvite = interviewInviteService.sendInvite(
				backendJob.getId(),
				new InviteCreateRequest(anaPublicId, "Convite para a vaga de Backend Python.")
		);
		interviewInviteService.acceptInvite(backendInvite.conviteId(), DemoDataSeeder.ANA_ID);
		interviewInviteService.scheduleInterview(
				backendInvite.conviteId(),
				DemoDataSeeder.RECRUITER_ALPHA_ID,
				new ScheduleInterviewRequest(defaultInterviewProposal(), null)
		);

		interviewInviteService.sendInvite(
				internshipJob.getId(),
				new InviteCreateRequest(anaPublicId, "Convite para a vaga de Estágio em Dados.")
		);

		interviewInviteService.sendInvite(
				frontendJob.getId(),
				new InviteCreateRequest(anaPublicId, "Convite para a vaga de Frontend React.")
		);

		log.info("Test scenario C2 ready for ana.silva@demo.local");
	}

	private JobVacancy seedInternshipJobIfMissing() {
		return jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(DemoDataSeeder.RECRUITER_BETA_ID).stream()
				.filter(job -> "Estágio em Dados".equals(job.getTitle()))
				.findFirst()
				.orElseGet(() -> {
					JobVacancy vacancy = new JobVacancy(
							"Estágio em Dados",
							DemoDataSeeder.RECRUITER_BETA_ID,
							"Vaga de estágio com foco em Python e análise de dados.",
							"Empresa Parceira Demo",
							WorkModality.REMOTO,
							EmploymentType.BOLSA_PROJETO,
							null,
							SeniorityLevel.ESTAGIO,
							1000,
							2000
					);
					vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 3, true, 2));
					return jobVacancyRepository.save(vacancy);
				});
	}

	private JobVacancy findBackendJob() {
		return jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(DemoDataSeeder.RECRUITER_ALPHA_ID).stream()
				.filter(job -> "Desenvolvedor Backend Python".equals(job.getTitle()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Backend demo job not found"));
	}

	private JobVacancy findFrontendJob() {
		return jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(DemoDataSeeder.RECRUITER_BETA_ID).stream()
				.filter(job -> "Desenvolvedor Frontend React".equals(job.getTitle()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Frontend demo job not found"));
	}

	private String publicIdFor(UUID candidateAccountId) {
		AnonymousProfile profile = anonymousProfileRepository.findByCandidateId(candidateAccountId)
				.orElseThrow(() -> new ProfileNotFoundException(candidateAccountId.toString()));
		return PublicIdFormatter.toPublicCandidateId(profile.getId());
	}

	private Instant defaultInterviewProposal() {
		return LocalDate.now().plusDays(1).atTime(10, 0).atZone(ZoneId.systemDefault()).toInstant();
	}
}
