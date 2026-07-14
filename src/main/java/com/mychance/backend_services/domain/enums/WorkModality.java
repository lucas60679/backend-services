package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum WorkModality {
	REMOTO("remoto", "Remoto"),
	HIBRIDO("hibrido", "Híbrido"),
	PRESENCIAL("presencial", "Presencial");

	private static final Map<String, WorkModality> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(item -> item.key, Function.identity()));

	private final String key;
	private final String displayName;

	WorkModality(String key, String displayName) {
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
	public static WorkModality fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		WorkModality modality = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (modality == null) {
			throw new IllegalArgumentException("Unknown work modality: " + value);
		}
		return modality;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
