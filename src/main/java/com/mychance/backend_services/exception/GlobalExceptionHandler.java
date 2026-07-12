package com.mychance.backend_services.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(JobNotFoundException.class)
	public ProblemDetail handleJobNotFound(JobNotFoundException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problem.setTitle("Job not found");
		return problem;
	}

	@ExceptionHandler(InvalidSkillException.class)
	public ProblemDetail handleInvalidSkill(InvalidSkillException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				"Skill must match the corporate vocabulary"
		);
		problem.setTitle("Invalid skill");
		problem.setProperty("skill", exception.getSkillKey());
		return problem;
	}

	@ExceptionHandler(ProfileNotFoundException.class)
	public ProblemDetail handleProfileNotFound(ProfileNotFoundException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problem.setTitle("Profile not found");
		return problem;
	}

	@ExceptionHandler(InviteNotFoundException.class)
	public ProblemDetail handleInviteNotFound(InviteNotFoundException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problem.setTitle("Invite not found");
		return problem;
	}

	@ExceptionHandler(InvalidInviteTransitionException.class)
	public ProblemDetail handleInvalidInviteTransition(InvalidInviteTransitionException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
		problem.setTitle("Invalid invite transition");
		return problem;
	}

	@ExceptionHandler(NlpMatchingUnavailableException.class)
	public ProblemDetail handleNlpUnavailable(NlpMatchingUnavailableException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.SERVICE_UNAVAILABLE,
				exception.getMessage()
		);
		problem.setTitle("Matching service unavailable");
		return problem;
	}

	@ExceptionHandler({InvalidEducationLevelException.class, InvalidRegionException.class})
	public ProblemDetail handleInvalidMetadata(RuntimeException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
		problem.setTitle("Invalid profile metadata");
		return problem;
	}

	@ExceptionHandler(EmailAlreadyRegisteredException.class)
	public ProblemDetail handleEmailAlreadyRegistered(EmailAlreadyRegisteredException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
		problem.setTitle("Email already registered");
		return problem;
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ProblemDetail handleInvalidCredentials(InvalidCredentialsException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
		problem.setTitle("Invalid credentials");
		return problem;
	}

	@ExceptionHandler(ResourceAccessDeniedException.class)
	public ProblemDetail handleResourceAccessDenied(ResourceAccessDeniedException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
		problem.setTitle("Access denied");
		return problem;
	}

	@ExceptionHandler(ProfileAlreadyExistsException.class)
	public ProblemDetail handleProfileAlreadyExists(ProfileAlreadyExistsException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
		problem.setTitle("Profile already exists");
		return problem;
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAuthentication(AuthenticationException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication required");
		problem.setTitle("Unauthorized");
		return problem;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDenied(AccessDeniedException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
		problem.setTitle("Forbidden");
		return problem;
	}

	@ExceptionHandler(DuplicateInviteException.class)
	public ProblemDetail handleDuplicateInvite(DuplicateInviteException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
		problem.setTitle("Duplicate invite");
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
		String details = exception.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.joining("; "));

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
		problem.setTitle("Validation failed");
		return problem;
	}
}
