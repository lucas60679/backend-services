package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum LanguageLevel {
	BASICO("basico", "Básico", 0),
	INTERMEDIARIO("intermediario", "Intermediário", 1),
	AVANCADO("avancado", "Avançado", 2),
	FLUENTE("fluente", "Fluente", 3);

	private static final Map<String, LanguageLevel> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(item -> item.key, Function.identity()));

	private final String key;
	private final String displayName;
	private final int rank;

	LanguageLevel(String key, String displayName, int rank) {
		this.key = key;
		this.displayName = displayName;
		this.rank = rank;
	}

	@JsonValue
	public String getKey() {
		return key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getRank() {
		return rank;
	}

	public boolean meetsOrExceeds(LanguageLevel required) {
		return required == null || this.rank >= required.rank;
	}

	@JsonCreator
	public static LanguageLevel fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		LanguageLevel level = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (level == null) {
			throw new IllegalArgumentException("Unknown language level: " + value);
		}
		return level;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
