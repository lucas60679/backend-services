package com.mychance.backend_services.exception;

import com.mychance.backend_services.domain.enums.InviteStatus;

public class InvalidInviteTransitionException extends RuntimeException {

	public InvalidInviteTransitionException(InviteStatus current, InviteStatus target) {
		super("Cannot transition invite from " + current + " to " + target);
	}
}
