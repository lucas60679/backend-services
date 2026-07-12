package com.mychance.backend_services.exception;

public class ProfileAlreadyExistsException extends RuntimeException {

	public ProfileAlreadyExistsException() {
		super("Candidate profile already exists for this account");
	}
}
