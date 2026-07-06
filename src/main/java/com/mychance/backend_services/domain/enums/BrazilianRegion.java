package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BrazilianRegion {
	NORTE("norte", "Região Norte"),
	NORDESTE("nordeste", "Região Nordeste"),
	CENTRO_OESTE("centro_oeste", "Região Centro-Oeste"),
	SUDESTE("sudeste", "Região Sudeste"),
	SUL("sul", "Região Sul");

	private static final Map<String, BrazilianRegion> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(region -> region.key, Function.identity()));

	private final String key;
	private final String displayName;

	BrazilianRegion(String key, String displayName) {
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
	public static BrazilianRegion fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		BrazilianRegion region = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (region == null) {
			throw new IllegalArgumentException("Unknown region: " + value);
		}
		return region;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
