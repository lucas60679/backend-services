package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.Candidate;
import com.mychance.backend_services.domain.entity.CandidateExperience;
import com.mychance.backend_services.domain.entity.CandidateProject;
import com.mychance.backend_services.domain.entity.CandidateSkill;
import com.mychance.backend_services.domain.enums.BrazilianRegion;
import com.mychance.backend_services.domain.enums.EducationLevel;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.exception.InvalidEducationLevelException;
import com.mychance.backend_services.exception.InvalidRegionException;
import com.mychance.backend_services.exception.InvalidSkillException;
import com.mychance.backend_services.exception.ProfileNotFoundException;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.repository.CandidateRepository;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class CandidateProfileService {

	private final CandidateRepository candidateRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final TextSanitizerService textSanitizerService;
	private final InterviewInviteService interviewInviteService;

	public CandidateProfileService(
			CandidateRepository candidateRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			TextSanitizerService textSanitizerService,
			InterviewInviteService interviewInviteService
	) {
		this.candidateRepository = candidateRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.textSanitizerService = textSanitizerService;
		this.interviewInviteService = interviewInviteService;
	}

	@Transactional
	public ProfileCreateResponse createProfile(ProfileCreateRequest request) {
		UUID candidateId = UUID.randomUUID();
		Candidate candidate = new Candidate(
				"Candidato " + candidateId.toString().substring(0, 8),
				"candidate-" + candidateId + "@mychance.local"
		);
		candidateRepository.save(candidate);

		AnonymousProfile profile = new AnonymousProfile(candidate);
		applyProfileData(request, profile);

		AnonymousProfile saved = anonymousProfileRepository.save(profile);
		return new ProfileCreateResponse(
				PublicIdFormatter.toPublicCandidateId(saved.getId()),
				"Perfil anonimizado criado com sucesso"
		);
	}

	@Transactional
	public ProfileCreateResponse updateProfile(String publicCandidateId, ProfileCreateRequest request) {
		AnonymousProfile profile = findProfileByPublicId(publicCandidateId);
		profile.getSkills().clear();
		profile.getExperiences().clear();
		profile.getProjects().clear();
		applyProfileData(request, profile);

		AnonymousProfile saved = anonymousProfileRepository.save(profile);
		interviewInviteService.invalidatePendingInvitesForProfile(saved);

		return new ProfileCreateResponse(
				PublicIdFormatter.toPublicCandidateId(saved.getId()),
				"Perfil anonimizado atualizado com sucesso"
		);
	}

	private void applyProfileData(ProfileCreateRequest request, AnonymousProfile profile) {
		mapSkills(request.competencias(), profile);
		mapExperiences(request, profile);
		mapProjects(request, profile);
		mapAnonymizedMetadata(request, profile);
		profile.setSalaryExpectationMin(request.pretensaoSalarialMinima());
	}

	private void mapAnonymizedMetadata(ProfileCreateRequest request, AnonymousProfile profile) {
		if (request.nivelEscolaridade() != null && !request.nivelEscolaridade().isBlank()) {
			if (!EducationLevel.isValidKey(request.nivelEscolaridade())) {
				throw new InvalidEducationLevelException(request.nivelEscolaridade());
			}
			profile.setEducationLevel(EducationLevel.fromKey(request.nivelEscolaridade()));
		}

		if (request.regiao() != null && !request.regiao().isBlank()) {
			if (!BrazilianRegion.isValidKey(request.regiao())) {
				throw new InvalidRegionException(request.regiao());
			}
			profile.setRegionState(BrazilianRegion.fromKey(request.regiao()));
		}
	}

	private AnonymousProfile findProfileByPublicId(String publicCandidateId) {
		String prefix = PublicIdFormatter.extractPrefix(publicCandidateId);
		return anonymousProfileRepository.findByPublicIdPrefix(prefix)
				.orElseThrow(() -> new ProfileNotFoundException(publicCandidateId));
	}

	private void mapSkills(Map<String, Integer> competencias, AnonymousProfile profile) {
		for (Map.Entry<String, Integer> entry : competencias.entrySet()) {
			if (!SkillName.isValidKey(entry.getKey())) {
				throw new InvalidSkillException(entry.getKey());
			}
			SkillName skillName = SkillName.fromKey(entry.getKey());
			profile.addSkill(new CandidateSkill(skillName, entry.getValue()));
		}
	}

	private void mapExperiences(ProfileCreateRequest request, AnonymousProfile profile) {
		request.experiencias().forEach(experience ->
				profile.addExperience(new CandidateExperience(experience.cargo(), experience.tempoMeses()))
		);
	}

	private void mapProjects(ProfileCreateRequest request, AnonymousProfile profile) {
		request.projetosDestaque().forEach(projectDescription -> {
			String sanitized = textSanitizerService.sanitize(projectDescription);
			profile.addProject(new CandidateProject(sanitized));
		});
	}
}
