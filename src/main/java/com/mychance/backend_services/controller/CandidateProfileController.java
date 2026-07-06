package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.service.CandidateProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateProfileController {

	private final CandidateProfileService candidateProfileService;

	public CandidateProfileController(CandidateProfileService candidateProfileService) {
		this.candidateProfileService = candidateProfileService;
	}

	@PostMapping("/profiles")
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileCreateResponse createProfile(@Valid @RequestBody ProfileCreateRequest request) {
		return candidateProfileService.createProfile(request);
	}

	@PutMapping("/profiles/{candidatoId}")
	public ProfileCreateResponse updateProfile(
			@PathVariable String candidatoId,
			@Valid @RequestBody ProfileCreateRequest request
	) {
		return candidateProfileService.updateProfile(candidatoId, request);
	}
}
