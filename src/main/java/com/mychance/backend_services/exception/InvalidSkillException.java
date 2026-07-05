package com.mychance.backend_services.exception;

public class InvalidSkillException extends RuntimeException {

	private final String skillKey;

	public InvalidSkillException(String skillKey) {
		super("Unknown or invalid skill: " + skillKey);
		this.skillKey = skillKey;
	}

	public String getSkillKey() {
		return skillKey;
	}
}
