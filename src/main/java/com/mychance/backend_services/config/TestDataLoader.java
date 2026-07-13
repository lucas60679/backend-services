package com.mychance.backend_services.config;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.domain.enums.UserRole;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@Profile("test")
public class TestDataLoader {

	private static final UUID TEST_RECRUITER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	@Bean
	CommandLineRunner seedMinimalJobForTests(
			AccountRepository accountRepository,
			PasswordEncoder passwordEncoder,
			JobVacancyRepository jobVacancyRepository
	) {
		return args -> {
			if (!accountRepository.existsById(TEST_RECRUITER_ID)) {
				accountRepository.save(Account.withId(
						TEST_RECRUITER_ID,
						"Recrutador Teste",
						"recruiter-test@mychance.local",
						passwordEncoder.encode("recruiter123"),
						UserRole.RECRUITER
				));
			}

			if (jobVacancyRepository.count() > 0) {
				return;
			}

			JobVacancy vacancy = new JobVacancy(
					"Desenvolvedor Backend Python",
					TEST_RECRUITER_ID,
					"Vaga de teste de integração.",
					12000
			);
			vacancy.addRequirement(new JobRequirement(SkillName.PYTHON, 5, true, 3));
			vacancy.addRequirement(new JobRequirement(SkillName.SQL, 4, true, 3));
			vacancy.addRequirement(new JobRequirement(SkillName.DOCKER, 3, false, 2));
			vacancy.addRequirement(new JobRequirement(SkillName.POSTGRESQL, 3, false, 2));
			jobVacancyRepository.save(vacancy);
		};
	}
}
