package com.mychance.backend_services.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BrazilianState {
	AC("ac", "Acre"),
	AL("al", "Alagoas"),
	AP("ap", "Amapá"),
	AM("am", "Amazonas"),
	BA("ba", "Bahia"),
	CE("ce", "Ceará"),
	DF("df", "Distrito Federal"),
	ES("es", "Espírito Santo"),
	GO("go", "Goiás"),
	MA("ma", "Maranhão"),
	MT("mt", "Mato Grosso"),
	MS("ms", "Mato Grosso do Sul"),
	MG("mg", "Minas Gerais"),
	PA("pa", "Pará"),
	PB("pb", "Paraíba"),
	PR("pr", "Paraná"),
	PE("pe", "Pernambuco"),
	PI("pi", "Piauí"),
	RJ("rj", "Rio de Janeiro"),
	RN("rn", "Rio Grande do Norte"),
	RS("rs", "Rio Grande do Sul"),
	RO("ro", "Rondônia"),
	RR("rr", "Roraima"),
	SC("sc", "Santa Catarina"),
	SP("sp", "São Paulo"),
	SE("se", "Sergipe"),
	TO("to", "Tocantins");

	private static final Map<String, BrazilianState> BY_KEY = Arrays.stream(values())
			.collect(Collectors.toMap(state -> state.key, Function.identity()));

	private final String key;
	private final String displayName;

	BrazilianState(String key, String displayName) {
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
	public static BrazilianState fromKey(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		BrazilianState state = BY_KEY.get(value.trim().toLowerCase(Locale.ROOT));
		if (state == null) {
			throw new IllegalArgumentException("Unknown state: " + value);
		}
		return state;
	}

	public static boolean isValidKey(String value) {
		return value != null && BY_KEY.containsKey(value.trim().toLowerCase(Locale.ROOT));
	}
}
