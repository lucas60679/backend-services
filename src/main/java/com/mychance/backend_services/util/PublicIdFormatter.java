package com.mychance.backend_services.util;

import java.util.UUID;

public final class PublicIdFormatter {

	private PublicIdFormatter() {
	}

	public static String toPublicCandidateId(UUID profileId) {
		String compact = profileId.toString().replace("-", "");
		return "usr_" + compact.substring(0, 8);
	}

	public static String extractPrefix(String publicCandidateId) {
		if (publicCandidateId == null || !publicCandidateId.startsWith("usr_") || publicCandidateId.length() < 12) {
			throw new IllegalArgumentException("Invalid public candidate id: " + publicCandidateId);
		}
		return publicCandidateId.substring(4, 12);
	}
}
