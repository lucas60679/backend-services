package com.mychance.backend_services.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "candidates")
public class Candidate {

	@Id
	private UUID id;

	@OneToOne(optional = false)
	@MapsId
	@JoinColumn(name = "id")
	private Account account;

	@OneToOne(mappedBy = "candidate")
	private AnonymousProfile anonymousProfile;

	protected Candidate() {
	}

	public Candidate(Account account) {
		this.account = account;
	}

	public UUID getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public String getFullName() {
		return account.getFullName();
	}

	public String getEmail() {
		return account.getEmail();
	}

	public AnonymousProfile getAnonymousProfile() {
		return anonymousProfile;
	}
}
