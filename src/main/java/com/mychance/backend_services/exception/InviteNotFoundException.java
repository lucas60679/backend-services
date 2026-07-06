package com.mychance.backend_services.exception;

import java.util.UUID;

public class InviteNotFoundException extends RuntimeException {

	public InviteNotFoundException(UUID inviteId) {
		super("Interview invite not found: " + inviteId);
	}
}
