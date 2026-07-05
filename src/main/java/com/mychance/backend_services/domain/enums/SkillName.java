package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SkillName {
	PYTHON("python", "Python"),
	SQL("sql", "SQL"),
	DOCKER("docker", "Docker"),
	POWERBI("powerbi", "Power BI"),
	STREAMLIT("streamlit", "Streamlit"),
	POSTGRESQL("postgresql", "PostgreSQL");

	private static final Map<String, SkillName> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(skill -> skill.key, Function.identity()));

	private final String key;
	private final String displayName;

	SkillName(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}

	@JsonValue
	public String getKey() {
		return key;
	}

	public String getDisplayName() {
		return displayName;
	}

	@JsonCreator
	public static SkillName fromKey(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Skill name cannot be blank");
		}
		SkillName skill = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (skill == null) {
			throw new IllegalArgumentException("Unknown skill: " + value);
		}
		return skill;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
