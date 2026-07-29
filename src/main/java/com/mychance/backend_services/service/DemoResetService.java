package com.mychance.backend_services.service;

import com.mychance.backend_services.config.DemoDataSeeder;
import com.mychance.backend_services.config.TestScenario;
import com.mychance.backend_services.config.TestScenarioSeeder;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.repository.CandidateRepository;
import com.mychance.backend_services.repository.InterviewInviteRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoResetService {

	private static final Logger log = LoggerFactory.getLogger(DemoResetService.class);

	private final InterviewInviteRepository interviewInviteRepository;
	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final CandidateRepository candidateRepository;
	private final AccountRepository accountRepository;
	private final DemoDataSeeder demoDataSeeder;
	private final TestScenarioSeeder testScenarioSeeder;

	public DemoResetService(
			InterviewInviteRepository interviewInviteRepository,
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			CandidateRepository candidateRepository,
			AccountRepository accountRepository,
			DemoDataSeeder demoDataSeeder,
			TestScenarioSeeder testScenarioSeeder
	) {
		this.interviewInviteRepository = interviewInviteRepository;
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.candidateRepository = candidateRepository;
		this.accountRepository = accountRepository;
		this.demoDataSeeder = demoDataSeeder;
		this.testScenarioSeeder = testScenarioSeeder;
	}

	@Transactional
	public void resetToInitialState() {
		resetToScenario(TestScenario.BASE);
	}

	@Transactional
	public void resetToScenario(TestScenario scenario) {
		log.warn("Resetting application data to scenario {}", scenario);

		interviewInviteRepository.deleteAll();
		jobVacancyRepository.deleteAll();
		anonymousProfileRepository.deleteAll();
		candidateRepository.deleteAll();
		accountRepository.deleteAll();

		demoDataSeeder.seed();
		testScenarioSeeder.seedScenario(scenario);

		log.info("Demo data reset completed for scenario {}", scenario);
	}
}
