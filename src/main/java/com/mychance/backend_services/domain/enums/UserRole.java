package com.mychance.backend_services.domain.enums;

public enum UserRole {
	CANDIDATE,
	RECRUITER,
	ADMIN;

	public static UserRole fromKey(String key) {
		return UserRole.valueOf(key.trim().toUpperCase());
	}

	public static boolean isValidKey(String key) {
		if (key == null || key.isBlank()) {
			return false;
		}
		try {
			fromKey(key);
			return true;
		} catch (IllegalArgumentException exception) {
			return false;
		}
	}
}
