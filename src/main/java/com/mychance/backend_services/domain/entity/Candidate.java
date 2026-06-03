package com.mychance.backend_services.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "candidates")
public class Candidate {

	@Id
	private UUID id;

	@Column(name = "full_name", nullable = false, length = 255)
	private String fullName;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@OneToOne(mappedBy = "candidate")
	private AnonymousProfile anonymousProfile;

	protected Candidate() {
	}

	public Candidate(String fullName, String email) {
		this.fullName = fullName;
		this.email = email;
	}

	@PrePersist
	void assignId() {
		if (id == null) {
			id = UUID.randomUUID();
		}
	}

	public UUID getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public AnonymousProfile getAnonymousProfile() {
		return anonymousProfile;
	}
}
