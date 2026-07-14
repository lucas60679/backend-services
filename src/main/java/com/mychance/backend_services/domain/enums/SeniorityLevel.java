package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SeniorityLevel {
	BOLSA_INICIACAO("bolsa_iniciacao", "Bolsa / Iniciação", 0),
	ESTAGIO("estagio", "Estágio", 1),
	JUNIOR("junior", "Júnior", 2),
	PLENO("pleno", "Pleno", 3),
	SENIOR("senior", "Sênior", 4),
	ESPECIALISTA("especialista", "Especialista", 5);

	private static final Map<String, SeniorityLevel> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(item -> item.key, Function.identity()));

	private final String key;
	private final String displayName;
	private final int rank;

	SeniorityLevel(String key, String displayName, int rank) {
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

	@JsonCreator
	public static SeniorityLevel fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		SeniorityLevel level = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (level == null) {
			throw new IllegalArgumentException("Unknown seniority level: " + value);
		}
		return level;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
