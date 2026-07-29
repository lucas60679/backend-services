package com.mychance.backend_services.service;

import com.mychance.backend_services.config.DemoDataSeeder;
import com.mychance.backend_services.config.TestScenario;
import com.mychance.backend_services.domain.enums.InviteStatus;
import com.mychance.backend_services.domain.enums.ScheduleStatus;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.InterviewInviteRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DemoResetServiceIntegrationTest {

	@Autowired
	private DemoResetService demoResetService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private InterviewInviteRepository interviewInviteRepository;

	@Test
	void resetRestoresInitialDemoDataset() {
		demoResetService.resetToScenario(TestScenario.BASE);

		assertThat(accountRepository.count()).isGreaterThanOrEqualTo(10);
		assertThat(jobVacancyRepository.count()).isGreaterThanOrEqualTo(2);
		assertThat(interviewInviteRepository.count()).isZero();
		assertThat(accountRepository.existsByEmailIgnoreCase("admin@mychance.local")).isTrue();
		assertThat(accountRepository.existsByEmailIgnoreCase("recruiter.teste@demo.local")).isTrue();
	}

	@Test
	void resetPreparesRecruiterScenarioR1() {
		demoResetService.resetToScenario(TestScenario.R1);

		assertThat(interviewInviteRepository.count()).isEqualTo(3);
		assertThat(interviewInviteRepository.findAll()).allSatisfy(invite -> {
			if (invite.getProfile().getCandidate().getId().equals(DemoDataSeeder.ANA_ID)) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.ACEITO);
				assertThat(invite.getScheduleStatus()).isEqualTo(ScheduleStatus.PROPOSED_BY_RECRUITER);
			}
			if (invite.getProfile().getCandidate().getId().equals(DemoDataSeeder.DIEGO_ID)) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.ENVIADO);
			}
			if (invite.getProfile().getCandidate().getId().equals(DemoDataSeeder.ELISA_ID)) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.RECUSADO);
			}
		});
	}

	@Test
	void resetPreparesCandidateScenarioC2() {
		demoResetService.resetToScenario(TestScenario.C2);

		assertThat(interviewInviteRepository.count()).isEqualTo(3);
		assertThat(jobVacancyRepository.findAll().stream().anyMatch(job -> "Estágio em Dados".equals(job.getTitle())))
				.isTrue();

		assertThat(interviewInviteRepository.findAll()).allSatisfy(invite -> {
			if ("Desenvolvedor Backend Python".equals(invite.getJobVacancy().getTitle())) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.ACEITO);
				assertThat(invite.getScheduleStatus()).isEqualTo(ScheduleStatus.PROPOSED_BY_RECRUITER);
			}
			if ("Estágio em Dados".equals(invite.getJobVacancy().getTitle())) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.ENVIADO);
			}
			if ("Desenvolvedor Frontend React".equals(invite.getJobVacancy().getTitle())) {
				assertThat(invite.getStatus()).isEqualTo(InviteStatus.ENVIADO);
			}
		});
	}
}
