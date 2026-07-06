package com.mychance.backend_services.controller;

import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.service.InterviewInviteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class InterviewInviteController {

	private final InterviewInviteService interviewInviteService;

	public InterviewInviteController(InterviewInviteService interviewInviteService) {
		this.interviewInviteService = interviewInviteService;
	}

	@PostMapping("/jobs/{jobId}/invites")
	@ResponseStatus(HttpStatus.CREATED)
	public InviteResponse sendInvite(
			@PathVariable UUID jobId,
			@Valid @RequestBody InviteCreateRequest request
	) {
		return interviewInviteService.sendInvite(jobId, request);
	}

	@PostMapping("/invites/{inviteId}/accept")
	public InviteResponse acceptInvite(@PathVariable UUID inviteId) {
		return interviewInviteService.acceptInvite(inviteId);
	}

	@PostMapping("/invites/{inviteId}/reject")
	public InviteResponse rejectInvite(@PathVariable UUID inviteId) {
		return interviewInviteService.rejectInvite(inviteId);
	}

	@GetMapping("/candidates/{candidatoId}/invites")
	public List<InviteResponse> listCandidateInvites(@PathVariable String candidatoId) {
		return interviewInviteService.listInvitesForCandidate(candidatoId);
	}

	@GetMapping("/jobs/{jobId}/invites")
	public List<InviteResponse> listJobInvites(@PathVariable UUID jobId) {
		return interviewInviteService.listInvitesForJob(jobId);
	}
}
