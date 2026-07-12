package com.mychance.backend_services.security;

import com.mychance.backend_services.domain.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long expirationHours;

	public JwtService(
			@Value("${mychance.jwt.secret}") String secret,
			@Value("${mychance.jwt.expiration-hours:24}") long expirationHours
	) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationHours = expirationHours;
	}

	public String generateToken(AuthenticatedUser user) {
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(user.id().toString())
				.claim("email", user.email())
				.claim("role", user.role().name())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(expirationHours, ChronoUnit.HOURS)))
				.signWith(secretKey)
				.compact();
	}

	public AuthenticatedUser parseUser(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return new AuthenticatedUser(
				UUID.fromString(claims.getSubject()),
				claims.get("email", String.class),
				UserRole.fromKey(claims.get("role", String.class))
		);
	}

	public boolean isValid(String token) {
		try {
			parseUser(token);
			return true;
		} catch (RuntimeException exception) {
			return false;
		}
	}
}
