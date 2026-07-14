package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EmploymentType {
	BOLSA_PROJETO("bolsa_projeto", "Bolsa / projeto acadêmico"),
	ESTAGIO("estagio", "Estágio"),
	CLT("clt", "CLT"),
	PJ("pj", "PJ"),
	FREELANCER("freelancer", "Freelancer");

	private static final Map<String, EmploymentType> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(item -> item.key, Function.identity()));

	private final String key;
	private final String displayName;

	EmploymentType(String key, String displayName) {
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
	public static EmploymentType fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		EmploymentType type = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (type == null) {
			throw new IllegalArgumentException("Unknown employment type: " + value);
		}
		return type;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
