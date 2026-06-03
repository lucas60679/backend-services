package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import com.mychance.backend_services.domain.entity.Candidate;
import com.mychance.backend_services.domain.entity.CandidateExperience;
import com.mychance.backend_services.domain.entity.CandidateProject;
import com.mychance.backend_services.domain.entity.CandidateSkill;
import com.mychance.backend_services.domain.enums.SkillName;
import com.mychance.backend_services.dto.request.ProfileCreateRequest;
import com.mychance.backend_services.dto.response.ProfileCreateResponse;
import com.mychance.backend_services.exception.InvalidSkillException;
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

	public CandidateProfileService(
			CandidateRepository candidateRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			TextSanitizerService textSanitizerService
	) {
		this.candidateRepository = candidateRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.textSanitizerService = textSanitizerService;
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
		mapSkills(request.competencias(), profile);
		mapExperiences(request, profile);
		mapProjects(request, profile);

		AnonymousProfile saved = anonymousProfileRepository.save(profile);
		return new ProfileCreateResponse(
				PublicIdFormatter.toPublicCandidateId(saved.getId()),
				"Perfil anonimizado criado com sucesso"
		);
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
