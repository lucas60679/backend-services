package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.InterviewInvite;
import com.mychance.backend_services.domain.entity.JobRequirement;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.InviteStatus;
import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.exception.InvalidInviteTransitionException;
import com.mychance.backend_services.exception.InviteNotFoundException;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.exception.ProfileNotFoundException;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.repository.InterviewInviteRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InterviewInviteService {

	private final InterviewInviteRepository interviewInviteRepository;
	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final MatchingAdapter matchingAdapter;
	private final MatchingEngine matchingEngine;

	public InterviewInviteService(
			InterviewInviteRepository interviewInviteRepository,
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			MatchingAdapter matchingAdapter,
			MatchingEngine matchingEngine
	) {
		this.interviewInviteRepository = interviewInviteRepository;
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.matchingAdapter = matchingAdapter;
		this.matchingEngine = matchingEngine;
	}

	@Transactional
	public InviteResponse sendInvite(UUID jobId, InviteCreateRequest request) {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdWithRequirements(jobId)
				.orElseThrow(() -> new JobNotFoundException(jobId));
		AnonymousProfile profile = findProfileByPublicId(request.candidatoId());

		InterviewInvite invite = new InterviewInvite(
				jobVacancy,
				profile,
				InviteStatus.ENVIADO,
				request.mensagem()
		);
		return toResponse(interviewInviteRepository.save(invite));
	}

	@Transactional
	public InviteResponse acceptInvite(UUID inviteId) {
		InterviewInvite invite = getInvite(inviteId);
		assertTransition(invite.getStatus(), InviteStatus.ACEITO);
		invite.setStatus(InviteStatus.ACEITO);
		return toResponse(invite);
	}

	@Transactional
	public InviteResponse rejectInvite(UUID inviteId) {
		InterviewInvite invite = getInvite(inviteId);
		assertTransition(invite.getStatus(), InviteStatus.RECUSADO);
		invite.setStatus(InviteStatus.RECUSADO);
		return toResponse(invite);
	}

	@Transactional(readOnly = true)
	public List<InviteResponse> listInvitesForCandidate(String publicCandidateId) {
		AnonymousProfile profile = findProfileByPublicId(publicCandidateId);
		return interviewInviteRepository.findByProfileId(profile.getId()).stream()
				.filter(invite -> invite.getStatus() != InviteStatus.INVALIDADO)
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<InviteResponse> listInvitesForJob(UUID jobId) {
		if (!jobVacancyRepository.existsById(jobId)) {
			throw new JobNotFoundException(jobId);
		}
		return interviewInviteRepository.findByJobId(jobId).stream()
				.filter(invite -> invite.getStatus() != InviteStatus.INVALIDADO)
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public void invalidatePendingInvitesForProfile(AnonymousProfile profile) {
		List<InterviewInvite> pendingInvites = interviewInviteRepository.findByProfileIdAndStatusIn(
				profile.getId(),
				List.of(InviteStatus.SUGERIDO, InviteStatus.ENVIADO)
		);

		for (InterviewInvite invite : pendingInvites) {
			JobVacancy jobVacancy = jobVacancyRepository.findByIdWithRequirements(invite.getJobVacancy().getId())
					.orElseThrow(() -> new JobNotFoundException(invite.getJobVacancy().getId()));
			double score = matchingEngine.computeScore(matchingAdapter.adapt(profile, jobVacancy), jobVacancy);
			if (score <= 0.0) {
				invite.setStatus(InviteStatus.INVALIDADO);
			}
		}
	}

	private InterviewInvite getInvite(UUID inviteId) {
		return interviewInviteRepository.findById(inviteId)
				.orElseThrow(() -> new InviteNotFoundException(inviteId));
	}

	private AnonymousProfile findProfileByPublicId(String publicCandidateId) {
		String prefix = PublicIdFormatter.extractPrefix(publicCandidateId);
		return anonymousProfileRepository.findByPublicIdPrefix(prefix)
				.orElseThrow(() -> new ProfileNotFoundException(publicCandidateId));
	}

	private void assertTransition(InviteStatus current, InviteStatus target) {
		if (current != InviteStatus.ENVIADO) {
			throw new InvalidInviteTransitionException(current, target);
		}
	}

	private InviteResponse toResponse(InterviewInvite invite) {
		return new InviteResponse(
				invite.getId(),
				invite.getJobVacancy().getId(),
				PublicIdFormatter.toPublicCandidateId(invite.getProfile().getId()),
				invite.getStatus().name(),
				invite.getMessage(),
				invite.getJobVacancy().getTitle(),
				invite.getCreatedAt(),
				invite.getUpdatedAt()
		);
	}
}
