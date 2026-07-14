package com.mychance.backend_services.exception;

public class InvalidStateException extends RuntimeException {

	public InvalidStateException(String value) {
		super("Invalid state: " + value);
	}
}
