package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.response.RecommendationResponse;
import com.mychance.backend_services.service.RecommendationService;
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

	public JobRecommendationController(RecommendationService recommendationService) {
		this.recommendationService = recommendationService;
	}

	@GetMapping("/{id}/recommendations")
	public List<RecommendationResponse> getRecommendations(@PathVariable UUID id) {
		return recommendationService.getRecommendations(id);
	}
}
