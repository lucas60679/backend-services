package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.InterviewInvite;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.InviteStatus;
import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.request.ScheduleInterviewRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.exception.DuplicateInviteException;
import com.mychance.backend_services.exception.InvalidInviteTransitionException;
import com.mychance.backend_services.exception.InviteNotFoundException;
import com.mychance.backend_services.exception.JobNotFoundException;
import com.mychance.backend_services.exception.ProfileNotFoundException;
import com.mychance.backend_services.exception.ResourceAccessDeniedException;
import com.mychance.backend_services.repository.AccountRepository;
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

	private enum InviteAudience {
		RECRUITER,
		CANDIDATE
	}

	private final InterviewInviteRepository interviewInviteRepository;
	private final JobVacancyRepository jobVacancyRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final AccountRepository accountRepository;
	private final MatchingAdapter matchingAdapter;
	private final MatchingEngine matchingEngine;

	public InterviewInviteService(
			InterviewInviteRepository interviewInviteRepository,
			JobVacancyRepository jobVacancyRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			AccountRepository accountRepository,
			MatchingAdapter matchingAdapter,
			MatchingEngine matchingEngine
	) {
		this.interviewInviteRepository = interviewInviteRepository;
		this.jobVacancyRepository = jobVacancyRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.accountRepository = accountRepository;
		this.matchingAdapter = matchingAdapter;
		this.matchingEngine = matchingEngine;
	}

	@Transactional
	public InviteResponse sendInvite(UUID jobId, InviteCreateRequest request) {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdWithRequirements(jobId)
				.orElseThrow(() -> new JobNotFoundException(jobId));
		AnonymousProfile profile = findProfileByPublicId(request.candidatoId());

		if (interviewInviteRepository.existsByJobVacancyIdAndProfileIdAndStatusIn(
				jobId,
				profile.getId(),
				List.of(InviteStatus.ENVIADO, InviteStatus.ACEITO)
		)) {
			throw new DuplicateInviteException(request.candidatoId());
		}

		InterviewInvite invite = new InterviewInvite(
				jobVacancy,
				profile,
				InviteStatus.ENVIADO,
				request.mensagem()
		);
		return toResponse(interviewInviteRepository.save(invite), InviteAudience.RECRUITER);
	}

	@Transactional
	public InviteResponse acceptInvite(UUID inviteId, UUID accountId) {
		InterviewInvite invite = getInvite(inviteId);
		assertCandidateOwnsInvite(invite, accountId);
		assertTransition(invite.getStatus(), InviteStatus.ACEITO);
		invite.setStatus(InviteStatus.ACEITO);
		return toResponse(invite, InviteAudience.CANDIDATE);
	}

	@Transactional
	public InviteResponse rejectInvite(UUID inviteId, UUID accountId) {
		InterviewInvite invite = getInvite(inviteId);
		assertCandidateOwnsInvite(invite, accountId);
		assertTransition(invite.getStatus(), InviteStatus.RECUSADO);
		invite.setStatus(InviteStatus.RECUSADO);
		return toResponse(invite, InviteAudience.CANDIDATE);
	}

	@Transactional
	public InviteResponse scheduleInterview(UUID inviteId, UUID recruiterId, ScheduleInterviewRequest request) {
		InterviewInvite invite = getInvite(inviteId);
		JobVacancy jobVacancy = invite.getJobVacancy();
		if (!jobVacancy.getRecruiterId().equals(recruiterId)) {
			throw new ResourceAccessDeniedException("Recruiter does not own this invite");
		}
		if (invite.getStatus() != InviteStatus.ACEITO) {
			throw new InvalidInviteTransitionException(invite.getStatus(), InviteStatus.ACEITO);
		}

		invite.setProposedInterviewAt(request.proposedInterviewAt());
		invite.setMeetingLink(request.meetingLink());
		return toResponse(invite, InviteAudience.RECRUITER);
	}

	@Transactional(readOnly = true)
	public List<InviteResponse> listInvitesForAccount(UUID accountId) {
		AnonymousProfile profile = anonymousProfileRepository.findByCandidateId(accountId)
				.orElseThrow(() -> new ProfileNotFoundException("me"));
		return interviewInviteRepository.findByProfileId(profile.getId()).stream()
				.filter(invite -> invite.getStatus() != InviteStatus.INVALIDADO)
				.map(invite -> toResponse(invite, InviteAudience.CANDIDATE))
				.toList();
	}

	@Transactional(readOnly = true)
	public List<InviteResponse> listInvitesForJob(UUID jobId) {
		if (!jobVacancyRepository.existsById(jobId)) {
			throw new JobNotFoundException(jobId);
		}
		return interviewInviteRepository.findByJobId(jobId).stream()
				.filter(invite -> invite.getStatus() != InviteStatus.INVALIDADO)
				.map(invite -> toResponse(invite, InviteAudience.RECRUITER))
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

	private void assertCandidateOwnsInvite(InterviewInvite invite, UUID accountId) {
		if (!invite.getProfile().getCandidate().getId().equals(accountId)) {
			throw new ResourceAccessDeniedException("Candidate does not own this invite");
		}
	}

	private void assertTransition(InviteStatus current, InviteStatus target) {
		if (current != InviteStatus.ENVIADO) {
			throw new InvalidInviteTransitionException(current, target);
		}
	}

	private InviteResponse toResponse(InterviewInvite invite, InviteAudience audience) {
		String candidatoNome = null;
		String candidatoEmail = null;
		String recruiterNome = null;
		String recruiterEmail = null;

		if (invite.getStatus() == InviteStatus.ACEITO) {
			if (audience == InviteAudience.RECRUITER) {
				candidatoNome = invite.getProfile().getCandidate().getFullName();
				candidatoEmail = invite.getProfile().getCandidate().getEmail();
			} else {
				Account recruiter = accountRepository.findById(invite.getJobVacancy().getRecruiterId())
						.orElse(null);
				if (recruiter != null) {
					recruiterNome = recruiter.getFullName();
					recruiterEmail = recruiter.getEmail();
				}
			}
		}

		return new InviteResponse(
				invite.getId(),
				invite.getJobVacancy().getId(),
				PublicIdFormatter.toPublicCandidateId(invite.getProfile().getId()),
				invite.getStatus().name(),
				invite.getMessage(),
				invite.getJobVacancy().getTitle(),
				candidatoNome,
				candidatoEmail,
				recruiterNome,
				recruiterEmail,
				invite.getProposedInterviewAt(),
				invite.getMeetingLink(),
				invite.getCreatedAt(),
				invite.getUpdatedAt()
		);
	}
}
