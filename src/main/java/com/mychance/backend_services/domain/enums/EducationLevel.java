package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EducationLevel {
	GRADUACAO_CONCLUIDA("graduacao_concluida", "Graduação Concluída"),
	POS_GRADUACAO_ANDAMENTO("pos_graduacao_andamento", "Pós-Graduação em Andamento"),
	POS_GRADUACAO_CONCLUIDA("pos_graduacao_concluida", "Pós-Graduação Concluída"),
	TECNICO("tecnico", "Ensino Técnico");

	private static final Map<String, EducationLevel> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(level -> level.key, Function.identity()));

	private final String key;
	private final String displayName;

	EducationLevel(String key, String displayName) {
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
	public static EducationLevel fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		EducationLevel level = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (level == null) {
			throw new IllegalArgumentException("Unknown education level: " + value);
		}
		return level;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
