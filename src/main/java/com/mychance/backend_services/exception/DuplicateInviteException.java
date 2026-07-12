package com.mychance.backend_services.exception;

public class DuplicateInviteException extends RuntimeException {

	public DuplicateInviteException(String candidatoId) {
		super("An active invite already exists for candidate " + candidatoId);
	}
}
