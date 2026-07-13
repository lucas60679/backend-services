package com.mychance.backend_services.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("docker")
public class DockerDataLoader {

	@Bean
	CommandLineRunner seedDemoDataForDocker(DemoDataSeeder demoDataSeeder) {
		return args -> demoDataSeeder.seed();
	}
}
