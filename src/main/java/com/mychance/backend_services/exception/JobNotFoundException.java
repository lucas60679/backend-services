package com.mychance.backend_services.exception;

import java.util.UUID;

public class JobNotFoundException extends RuntimeException {

	public JobNotFoundException(UUID jobId) {
		super("Job vacancy not found: " + jobId);
	}
}
