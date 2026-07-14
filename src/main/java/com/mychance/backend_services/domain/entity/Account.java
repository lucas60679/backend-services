package com.mychance.backend_services.domain.entity;

import com.mychance.backend_services.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "full_name", nullable = false, length = 255)
	private String fullName;

	@Column(name = "phone", length = 30)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserRole role;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected Account() {
	}

	public Account(String fullName, String email, String passwordHash, UserRole role) {
		this.fullName = fullName;
		this.email = email;
		this.passwordHash = passwordHash;
		this.role = role;
	}

	public Account(String fullName, String email, String passwordHash, UserRole role, String phone) {
		this(fullName, email, passwordHash, role);
		this.phone = phone;
	}

	public static Account withId(UUID id, String fullName, String email, String passwordHash, UserRole role) {
		Account account = new Account(fullName, email, passwordHash, role);
		account.id = id;
		return account;
	}

	public static Account withId(
			UUID id,
			String fullName,
			String email,
			String passwordHash,
			UserRole role,
			String phone
	) {
		Account account = withId(id, fullName, email, passwordHash, role);
		account.phone = phone;
		return account;
	}

	@PrePersist
	void assignDefaults() {
		if (id == null) {
			id = UUID.randomUUID();
		}
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getFullName() {
		return fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserRole getRole() {
		return role;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
