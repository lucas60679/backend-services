package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum LanguageName {
	PORTUGUES("portugues", "Português"),
	INGLES("ingles", "Inglês"),
	ESPANHOL("espanhol", "Espanhol"),
	FRANCES("frances", "Francês"),
	ALEMAO("alemao", "Alemão"),
	ITALIANO("italiano", "Italiano"),
	MANDARIM("mandarim", "Mandarim"),
	JAPONES("japones", "Japonês"),
	COREANO("coreano", "Coreano"),
	ARABICO("arabico", "Árabe");

	private static final Map<String, LanguageName> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(item -> item.key, Function.identity()));

	private final String key;
	private final String displayName;

	LanguageName(String key, String displayName) {
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
	public static LanguageName fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		LanguageName language = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (language == null) {
			throw new IllegalArgumentException("Unknown language: " + value);
		}
		return language;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
