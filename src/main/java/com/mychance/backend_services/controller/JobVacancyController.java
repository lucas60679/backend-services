package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.request.JobCreateRequest;
import com.mychance.backend_services.dto.response.JobCreateResponse;
import com.mychance.backend_services.service.JobVacancyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobVacancyController {

	private final JobVacancyService jobVacancyService;

	public JobVacancyController(JobVacancyService jobVacancyService) {
		this.jobVacancyService = jobVacancyService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public JobCreateResponse createJob(@Valid @RequestBody JobCreateRequest request) {
		return jobVacancyService.createJob(request);
	}
}
