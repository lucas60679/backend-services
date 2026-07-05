package com.mychance.backend_services.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TextSanitizerService {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
			Pattern.CASE_INSENSITIVE
	);

	private static final Pattern URL_PATTERN = Pattern.compile(
			"(https?://|www\\.)\\S+|\\b(linkedin|github|gitlab|bitbucket)\\.com/\\S+",
			Pattern.CASE_INSENSITIVE
	);

	public String sanitize(String text) {
		if (text == null || text.isBlank()) {
			return text;
		}
		String sanitized = EMAIL_PATTERN.matcher(text).replaceAll("[email removido]");
		sanitized = URL_PATTERN.matcher(sanitized).replaceAll("[link removido]");
		return sanitized.trim();
	}
}
