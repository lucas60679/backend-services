package com.mychance.backend_services.exception;

public class InvitePreviouslyRejectedException extends RuntimeException {

	public InvitePreviouslyRejectedException(String candidatoId) {
		super("Candidate " + candidatoId + " previously rejected an invite for this job");
	}
}
