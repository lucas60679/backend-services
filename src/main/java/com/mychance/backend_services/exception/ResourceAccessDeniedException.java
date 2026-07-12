package com.mychance.backend_services.exception;

public class ResourceAccessDeniedException extends RuntimeException {

	public ResourceAccessDeniedException(String message) {
		super(message);
	}
}
