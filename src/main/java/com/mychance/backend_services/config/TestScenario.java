package com.mychance.backend_services.config;

public enum TestScenario {
	BASE,
	R1,
	C2;

	public static TestScenario fromKey(String key) {
		if (key == null || key.isBlank()) {
			return BASE;
		}
		return TestScenario.valueOf(key.trim().toUpperCase());
	}

	public static boolean isValidKey(String key) {
		if (key == null || key.isBlank()) {
			return true;
		}
		try {
			fromKey(key);
			return true;
		} catch (IllegalArgumentException exception) {
			return false;
		}
	}
}
