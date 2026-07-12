package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.security.AuthenticatedUser;
import com.mychance.backend_services.service.JobVacancyService;
import com.mychance.backend_services.service.RecommendationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobRecommendationController {

	private final RecommendationService recommendationService;
	private final JobVacancyService jobVacancyService;

	public JobRecommendationController(
			RecommendationService recommendationService,
			JobVacancyService jobVacancyService
	) {
		this.recommendationService = recommendationService;
		this.jobVacancyService = jobVacancyService;
	}

	@GetMapping("/{id}/recommendations")
	public List<RecommendationResponse> getRecommendations(
			@AuthenticationPrincipal AuthenticatedUser user,
			@PathVariable UUID id
	) {
		jobVacancyService.getOwnedJob(user.id(), id);
		return recommendationService.getRecommendations(id);
	}
}
