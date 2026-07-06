package com.mychance.backend_services.controller;

import com.mychance.backend_services.repository.JobVacancyRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dev")
@Profile({"dev", "docker"})
public class DevJobController {

	private final JobVacancyRepository jobVacancyRepository;

	public DevJobController(JobVacancyRepository jobVacancyRepository) {
		this.jobVacancyRepository = jobVacancyRepository;
	}

	@GetMapping("/sample-job-id")
	public Map<String, UUID> sampleJobId() {
		return jobVacancyRepository.findAll().stream()
				.findFirst()
				.map(job -> Map.of("job_id", job.getId()))
				.orElse(Map.of());
	}
}
