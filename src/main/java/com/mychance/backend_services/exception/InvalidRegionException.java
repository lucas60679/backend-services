package com.mychance.backend_services.exception;

public class InvalidRegionException extends RuntimeException {

	public InvalidRegionException(String value) {
		super("Invalid region: " + value);
	}
}
