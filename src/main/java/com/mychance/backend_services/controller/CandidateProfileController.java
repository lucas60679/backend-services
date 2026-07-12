package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.MyProfileResponse;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.security.AuthenticatedUser;
import com.mychance.backend_services.service.CandidateProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ProfileCreateResponse createProfile(
			@AuthenticationPrincipal AuthenticatedUser user,
			@Valid @RequestBody ProfileCreateRequest request
	) {
		return candidateProfileService.createProfile(user.id(), request);
	}

	@PutMapping("/profiles/me")
	public ProfileCreateResponse updateProfile(
			@AuthenticationPrincipal AuthenticatedUser user,
			@Valid @RequestBody ProfileCreateRequest request
	) {
		return candidateProfileService.updateProfile(user.id(), request);
	}

	@GetMapping("/me/profile")
	public MyProfileResponse getMyProfile(@AuthenticationPrincipal AuthenticatedUser user) {
		return candidateProfileService.getMyProfile(user.id());
	}
}
