package com.mychance.backend_services.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PublicIdFormatterTest {

	@Test
	void formatsPublicCandidateIdWithUsrPrefix() {
		UUID profileId = UUID.fromString("98a72b3c-1234-5678-9abc-def012345678");

		String publicId = PublicIdFormatter.toPublicCandidateId(profileId);

		assertThat(publicId).isEqualTo("usr_98a72b3c");
	}
}
