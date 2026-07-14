package com.mychance.backend_services.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class ContactNormalizer {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$"
	);

	private ContactNormalizer() {
	}

	public static String normalizeEmail(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim().toLowerCase(Locale.ROOT);
	}

	public static boolean isValidEmail(String value) {
		String email = normalizeEmail(value);
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}

	public static String normalizePhone(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}

		String digits = value.replaceAll("\\D", "");
		if (digits.startsWith("55") && digits.length() >= 12) {
			digits = digits.substring(2);
		}

		if (digits.length() == 11) {
			return "(" + digits.substring(0, 2) + ") "
					+ digits.substring(2, 7) + "-"
					+ digits.substring(7);
		}
		if (digits.length() == 10) {
			return "(" + digits.substring(0, 2) + ") "
					+ digits.substring(2, 6) + "-"
					+ digits.substring(6);
		}
		return null;
	}

	public static boolean isValidPhone(String value) {
		return normalizePhone(value) != null;
	}
}
