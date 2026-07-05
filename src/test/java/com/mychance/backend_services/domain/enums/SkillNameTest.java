package com.mychance.backend_services.domain.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkillNameTest {

	@ParameterizedTest
	@ValueSource(strings = {"python", "PYTHON", " Python "})
	void resolvesSkillKeysCaseInsensitively(String rawKey) {
		assertThat(SkillName.fromKey(rawKey)).isEqualTo(SkillName.PYTHON);
	}

	@Test
	void rejectsUnknownSkillKeys() {
		assertThatThrownBy(() -> SkillName.fromKey("kubernetes"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Unknown skill");
	}

	@Test
	void rejectsBlankSkillKeys() {
		assertThatThrownBy(() -> SkillName.fromKey("  "))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("blank");
	}

	@Test
	void validatesKnownKeys() {
		assertThat(SkillName.isValidKey("docker")).isTrue();
		assertThat(SkillName.isValidKey("invalid")).isFalse();
		assertThat(SkillName.isValidKey(null)).isFalse();
	}

	@Test
	void exposesDisplayNamesForApiResponses() {
		assertThat(SkillName.POWERBI.getDisplayName()).isEqualTo("Power BI");
		assertThat(SkillName.POSTGRESQL.getDisplayName()).isEqualTo("PostgreSQL");
	}
}
