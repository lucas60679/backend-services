package com.mychance.backend_services.util;

import java.util.UUID;

public final class PublicIdFormatter {

	private PublicIdFormatter() {
	}

	public static String toPublicCandidateId(UUID profileId) {
		String compact = profileId.toString().replace("-", "");
		return "usr_" + compact.substring(0, 8);
	}
}
