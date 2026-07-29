package com.mychance.backend_services.service;

import com.mychance.backend_services.domain.entity.Account;
import com.mychance.backend_services.domain.enums.UserRole;
import com.mychance.backend_services.dto.request.LoginRequest;
import com.mychance.backend_services.dto.request.RegisterRequest;
import com.mychance.backend_services.dto.response.AuthResponse;
import com.mychance.backend_services.exception.EmailAlreadyRegisteredException;
import com.mychance.backend_services.exception.InvalidCredentialsException;
import com.mychance.backend_services.repository.AccountRepository;
import com.mychance.backend_services.repository.AnonymousProfileRepository;
import com.mychance.backend_services.security.AuthenticatedUser;
import com.mychance.backend_services.security.JwtService;
import com.mychance.backend_services.util.ContactNormalizer;
import com.mychance.backend_services.util.PublicIdFormatter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

	private final AccountRepository accountRepository;
	private final AnonymousProfileRepository anonymousProfileRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(
			AccountRepository accountRepository,
			AnonymousProfileRepository anonymousProfileRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService
	) {
		this.accountRepository = accountRepository;
		this.anonymousProfileRepository = anonymousProfileRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (!UserRole.isValidKey(request.role())) {
			throw new IllegalArgumentException("Invalid role: " + request.role());
		}

		UserRole role = UserRole.fromKey(request.role());

		if (role == UserRole.ADMIN) {
			throw new IllegalArgumentException("Registro não permitido para este perfil.");
		}

		String email = ContactNormalizer.normalizeEmail(request.email());
		if (!ContactNormalizer.isValidEmail(email)) {
			throw new IllegalArgumentException("Informe um e-mail válido.");
		}
		if (accountRepository.existsByEmailIgnoreCase(email)) {
			throw new EmailAlreadyRegisteredException(email);
		}

		String phone = ContactNormalizer.normalizePhone(request.telefone());
		if (role == UserRole.CANDIDATE) {
			if (phone == null) {
				throw new IllegalArgumentException(
						"Informe um telefone válido com DDD (10 ou 11 dígitos)."
				);
			}
		}

		Account account = new Account(
				request.nome().trim(),
				email,
				passwordEncoder.encode(request.senha()),
				role,
				phone
		);
		Account saved = accountRepository.save(account);
		return buildAuthResponse(saved);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		String email = ContactNormalizer.normalizeEmail(request.email());
		Account account = accountRepository.findByEmailIgnoreCase(email != null ? email : "")
				.orElseThrow(InvalidCredentialsException::new);

		if (!passwordEncoder.matches(request.senha(), account.getPasswordHash())) {
			throw new InvalidCredentialsException();
		}

		return buildAuthResponse(account);
	}

	@Transactional(readOnly = true)
	public AuthResponse getCurrentUser(UUID accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(InvalidCredentialsException::new);
		return buildAuthResponse(account);
	}

	private AuthResponse buildAuthResponse(Account account) {
		AuthenticatedUser authenticatedUser = new AuthenticatedUser(
				account.getId(),
				account.getEmail(),
				account.getRole()
		);
		String token = jwtService.generateToken(authenticatedUser);
		String candidatoId = resolveCandidatoId(account.getId(), account.getRole());

		return new AuthResponse(
				token,
				account.getId(),
				account.getRole().name(),
				account.getFullName(),
				candidatoId
		);
	}

	private String resolveCandidatoId(UUID accountId, UserRole role) {
		if (role != UserRole.CANDIDATE) {
			return null;
		}
		return anonymousProfileRepository.findByCandidateId(accountId)
				.map(profile -> PublicIdFormatter.toPublicCandidateId(profile.getId()))
				.orElse(null);
	}
}
