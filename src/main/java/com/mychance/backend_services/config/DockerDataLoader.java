package com.mychance.backend_services.config;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.domain.enums.UserRole;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@Profile("docker")
public class DockerDataLoader {

	private static final Logger log = LoggerFactory.getLogger(DockerDataLoader.class);
	private static final UUID DEMO_RECRUITER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	@Bean
	CommandLineRunner seedSampleJobForDocker(
			AccountRepository accountRepository,
			PasswordEncoder passwordEncoder,
			JobVacancyRepository jobVacancyRepository
	) {
		return args -> {
			if (!accountRepository.existsById(DEMO_RECRUITER_ID)) {
				Account recruiter = Account.withId(
						DEMO_RECRUITER_ID,
						"Recrutador Demo",
						"recruiter@mychance.local",
						passwordEncoder.encode("recruiter123"),
						UserRole.RECRUITER
				);
				accountRepository.save(recruiter);
				log.info("Demo recruiter seeded for Docker: {} / recruiter123", recruiter.getEmail());
			}

			if (jobVacancyRepository.count() > 0) {
				return;
			}

			JobVacancy vacancy = new JobVacancy(
					"Desenvolvedor Backend Python",
					DEMO_RECRUITER_ID,
					"Vaga inicial para demonstração no ambiente Docker.",
					12000
			);
			vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 3));
			vacancy.addRequirement(new JobRequirement(SkillName.SQL, 4, true, 3));
			vacancy.addRequirement(new JobRequirement(SkillName.DOCKER, 3, false, 2));
			vacancy.addRequirement(new JobRequirement(SkillName.POSTGRESQL, 3, false, 2));

			JobVacancy saved = jobVacancyRepository.save(vacancy);
			log.info("Sample job seeded for Docker environment: {}", saved.getId());
		};
	}
}
