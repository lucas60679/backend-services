package com.mychance.backend_services.exception;

public class NlpMatchingUnavailableException extends RuntimeException {

	public NlpMatchingUnavailableException(String message) {
		super(message);
	}

	public NlpMatchingUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
