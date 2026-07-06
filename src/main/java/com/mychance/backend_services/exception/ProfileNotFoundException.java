package com.mychance.backend_services.exception;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException {

	public ProfileNotFoundException(String publicCandidateId) {
		super("Candidate profile not found: " + publicCandidateId);
	}

	public ProfileNotFoundException(UUID profileId) {
		super("Candidate profile not found: " + profileId);
	}
}
