package com.mychance.backend_services.config;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.domain.enums.UserRole;
import com.mychance.backend_services.dto.request.ExperienceRequest;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.service.CandidateProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class DemoDataSeeder {

	private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

	public static final UUID RECRUITER_ALPHA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
	public static final UUID RECRUITER_BETA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

	public static final String DEMO_RECRUITER_PASSWORD = "recruiter123";
	public static final String DEMO_CANDIDATE_PASSWORD = "candidato123";

	private final AccountRepository accountRepository;
	private final JobVacancyRepository jobVacancyRepository;
	private final CandidateProfileService candidateProfileService;
	private final PasswordEncoder passwordEncoder;

	public DemoDataSeeder(
			AccountRepository accountRepository,
			JobVacancyRepository jobVacancyRepository,
			CandidateProfileService candidateProfileService,
			PasswordEncoder passwordEncoder
	) {
		this.accountRepository = accountRepository;
		this.jobVacancyRepository = jobVacancyRepository;
		this.candidateProfileService = candidateProfileService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void seed() {
		seedRecruiter(
				RECRUITER_ALPHA_ID,
				"Recrutador Demo",
				"recruiter@mychance.local"
		);
		seedRecruiter(
				RECRUITER_BETA_ID,
				"Recrutadora Beta",
				"recruiter2@mychance.local"
		);

		seedBackendJobIfMissing();
		seedFrontendJobIfMissing();

		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1"),
				"Ana Silva",
				"ana.silva@demo.local",
				new ProfileCreateRequest(
						Map.of("python", 5, "sql", 5, "docker", 4, "postgresql", 4),
						List.of(new ExperienceRequest("Desenvolvedor Backend Pleno", 30)),
						List.of("API REST em Python com PostgreSQL, testes automatizados e deploy em containers."),
						"graduacao_concluida",
						"sudeste",
						10000
				)
		);
		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2"),
				"Bruno Costa",
				"bruno.costa@demo.local",
				new ProfileCreateRequest(
						Map.of("python", 1, "sql", 5, "docker", 2),
						List.of(new ExperienceRequest("Desenvolvedor Backend Júnior", 14)),
						List.of("Scripts de integração com foco em SQL e automações internas."),
						"graduacao_concluida",
						"nordeste",
						7000
				)
		);
		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3"),
				"Carla Mendes",
				"carla.mendes@demo.local",
				new ProfileCreateRequest(
						Map.of("python", 5, "sql", 5, "docker", 3),
						List.of(new ExperienceRequest("Desenvolvedor Backend Pleno", 36)),
						List.of("Microsserviços Python com mensageria e observabilidade."),
						"pos_graduacao_concluida",
						"sul",
						15000
				)
		);
		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4"),
				"Diego Santos",
				"diego.santos@demo.local",
				new ProfileCreateRequest(
						Map.of("python", 4, "sql", 4, "docker", 3, "fastapi", 4),
						List.of(new ExperienceRequest("Desenvolvedor Backend Júnior", 18)),
						List.of("Serviços FastAPI com autenticação JWT e integração com filas."),
						"graduacao_concluida",
						"centro_oeste",
						8500
				)
		);
		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5"),
				"Elisa Ferreira",
				"elisa.ferreira@demo.local",
				new ProfileCreateRequest(
						Map.of("python", 4, "sql", 4, "postgresql", 3),
						List.of(new ExperienceRequest("Desenvolvedor Backend Júnior", 20)),
						List.of("ETL com Python e SQL para pipelines de dados operacionais."),
						"graduacao_concluida",
						"sudeste",
						9000
				)
		);
		seedCandidateProfile(
				UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa6"),
				"Felipe Rocha",
				"felipe.rocha@demo.local",
				new ProfileCreateRequest(
						Map.of("react", 5, "typescript", 4, "javascript", 5, "nodejs", 3),
						List.of(new ExperienceRequest("Desenvolvedor Frontend Pleno", 28)),
						List.of("SPA em React com TypeScript, testes de componente e CI/CD."),
						"graduacao_concluida",
						"sudeste",
						11000
				)
		);

		log.info("Demo dataset ready. Recruiters: recruiter@mychance.local, recruiter2@mychance.local (senha: {})",
				DEMO_RECRUITER_PASSWORD);
		log.info("Demo candidates: *@demo.local (senha: {})", DEMO_CANDIDATE_PASSWORD);
	}

	private void seedRecruiter(UUID id, String name, String email) {
		if (accountRepository.existsById(id) || accountRepository.existsByEmailIgnoreCase(email)) {
			return;
		}
		accountRepository.save(Account.withId(
				id,
				name,
				email,
				passwordEncoder.encode(DEMO_RECRUITER_PASSWORD),
				UserRole.RECRUITER
		));
		log.info("Demo recruiter seeded: {} / {}", email, DEMO_RECRUITER_PASSWORD);
	}

	private void seedBackendJobIfMissing() {
		if (jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(RECRUITER_ALPHA_ID).stream()
				.anyMatch(job -> "Desenvolvedor Backend Python".equals(job.getTitle()))) {
			return;
		}

		JobVacancy vacancy = new JobVacancy(
				"Desenvolvedor Backend Python",
				RECRUITER_ALPHA_ID,
				"Buscamos profissional para APIs Python, SQL e boas práticas de deploy. "
						+ "Triagem 100% anônima por competências técnicas.",
				12000
		);
		vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.SQL, 4, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.DOCKER, 3, false, 2));
		vacancy.addRequirement(new JobRequirement(SkillName.POSTGRESQL, 3, false, 2));

		JobVacancy saved = jobVacancyRepository.save(vacancy);
		log.info("Demo backend job seeded: {} ({})", saved.getTitle(), saved.getId());
	}

	private void seedFrontendJobIfMissing() {
		if (jobVacancyRepository.findByRecruiterIdOrderByTitleAsc(RECRUITER_BETA_ID).stream()
				.anyMatch(job -> "Desenvolvedor Frontend React".equals(job.getTitle()))) {
			return;
		}

		JobVacancy vacancy = new JobVacancy(
				"Desenvolvedor Frontend React",
				RECRUITER_BETA_ID,
				"Vaga para construção de interfaces React/TypeScript com foco em acessibilidade "
						+ "e integração com APIs REST.",
				13000
		);
		vacancy.addRequirement(new JobRequirement(SkillName.REACT, 5, true, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.TYPESCRIPT, 4, true, 2));
		vacancy.addRequirement(new JobRequirement(SkillName.JAVASCRIPT, 4, false, 3));
		vacancy.addRequirement(new JobRequirement(SkillName.GIT, 3, false, 2));

		JobVacancy saved = jobVacancyRepository.save(vacancy);
		log.info("Demo frontend job seeded: {} ({})", saved.getTitle(), saved.getId());
	}

	private void seedCandidateProfile(UUID accountId, String name, String email, ProfileCreateRequest profile) {
		if (accountRepository.existsByEmailIgnoreCase(email)) {
			return;
		}

		Account account = Account.withId(
				accountId,
				name,
				email,
				passwordEncoder.encode(DEMO_CANDIDATE_PASSWORD),
				UserRole.CANDIDATE
		);
		accountRepository.save(account);
		candidateProfileService.createProfile(accountId, profile);
		log.info("Demo candidate seeded: {} / {}", email, DEMO_CANDIDATE_PASSWORD);
	}
}
