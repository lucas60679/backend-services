package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.JobVacancy;
import com.mychance.backend_services.domain.enums.InviteStatus;
import com.mychance.backend_services.dto.request.ExperienceRequest;
import com.mychance.backend_services.dto.request.InviteCreateRequest;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.InviteResponse;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.exception.InvalidInviteTransitionException;
import com.mychance.backend_services.exception.InvitePreviouslyRejectedException;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.InterviewInviteRepository;
import com.mychance.backend_services.repository.JobVacancyRepository;
import com.mychance.backend_services.support.AccountTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class InterviewInviteServiceIntegrationTest {

	@Autowired
	private InterviewInviteService interviewInviteService;

	@Autowired
	private CandidateProfileService candidateProfileService;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private InterviewInviteRepository interviewInviteRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void sendsAcceptsAndRejectsInvites() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		Account account = accountRepository.save(AccountTestBuilder.candidateAccount("invite-1@test.local"));
		ProfileCreateResponse profile = createProfile(account, "invite-1@test.local");

		InviteResponse sent = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Gostaríamos de conversar.")
		);
		assertThat(sent.status()).isEqualTo(InviteStatus.ENVIADO.name());

		InviteResponse accepted = interviewInviteService.acceptInvite(sent.conviteId(), account.getId());
		assertThat(accepted.status()).isEqualTo(InviteStatus.ACEITO.name());

		Account secondAccount = accountRepository.save(AccountTestBuilder.candidateAccount("invite-2@test.local"));
		ProfileCreateResponse secondProfile = createProfile(secondAccount, "invite-2@test.local");
		InviteResponse secondInvite = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(secondProfile.candidatoId(), "Segunda proposta")
		);
		InviteResponse rejected = interviewInviteService.rejectInvite(secondInvite.conviteId(), secondAccount.getId());
		assertThat(rejected.status()).isEqualTo(InviteStatus.RECUSADO.name());
	}

	@Test
	void invalidatesPendingInviteWhenProfileBecomesIncompatible() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		Account account = accountRepository.save(AccountTestBuilder.candidateAccount("invite-3@test.local"));
		ProfileCreateResponse profile = createProfile(account, "invite-3@test.local");

		InviteResponse sent = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Proposta")
		);

		candidateProfileService.updateProfile(account.getId(), new ProfileCreateRequest(
				Map.of("python", 1, "sql", 5),
				List.of(new ExperienceRequest("Dev", 24)),
				List.of("Perfil atualizado"),
				null,
				null,
				5000
		));

		var invite = interviewInviteRepository.findById(sent.conviteId()).orElseThrow();
		assertThat(invite.getStatus()).isEqualTo(InviteStatus.INVALIDADO);
	}

	@Test
	void blocksAcceptAfterReject() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		Account account = accountRepository.save(AccountTestBuilder.candidateAccount("invite-4@test.local"));
		ProfileCreateResponse profile = createProfile(account, "invite-4@test.local");

		InviteResponse sent = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Proposta")
		);
		interviewInviteService.rejectInvite(sent.conviteId(), account.getId());

		UUID inviteId = sent.conviteId();
		assertThatThrownBy(() -> interviewInviteService.acceptInvite(inviteId, account.getId()))
				.isInstanceOf(InvalidInviteTransitionException.class);
	}

	@Test
	void blocksResendAfterReject() {
		JobVacancy job = jobVacancyRepository.findAll().get(0);
		Account account = accountRepository.save(AccountTestBuilder.candidateAccount("invite-5@test.local"));
		ProfileCreateResponse profile = createProfile(account, "invite-5@test.local");

		InviteResponse sent = interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Proposta")
		);
		interviewInviteService.rejectInvite(sent.conviteId(), account.getId());

		assertThatThrownBy(() -> interviewInviteService.sendInvite(
				job.getId(),
				new InviteCreateRequest(profile.candidatoId(), "Nova proposta")
		)).isInstanceOf(InvitePreviouslyRejectedException.class);
	}

	private ProfileCreateResponse createProfile(Account account, String ignoredEmail) {
		return candidateProfileService.createProfile(account.getId(), new ProfileCreateRequest(
				Map.of("python", 5, "sql", 5),
				List.of(new ExperienceRequest("Dev", 24)),
				List.of("Projeto anonimizado"),
				null,
				null,
				5000
		));
	}
}
