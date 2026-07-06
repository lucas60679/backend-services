package com.mychance.backend_services.exception;

public class InvalidEducationLevelException extends RuntimeException {

	public InvalidEducationLevelException(String value) {
		super("Invalid education level: " + value);
	}
}
