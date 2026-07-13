package com.mychance.backend_services.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod & !test")
public class DevDataLoader {

	@Bean
	CommandLineRunner seedDemoDataForDev(DemoDataSeeder demoDataSeeder) {
		return args -> demoDataSeeder.seed();
	}
}
