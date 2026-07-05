package com.mychance.backend_services.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidate_experiences")
public class CandidateExperience {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private AnonymousProfile profile;

	@Column(name = "role_title", nullable = false, length = 150)
	private String roleTitle;

	@Column(name = "duration_months", nullable = false)
	private int durationMonths;

	protected CandidateExperience() {
	}

	public CandidateExperience(String roleTitle, int durationMonths) {
		this.roleTitle = roleTitle;
		this.durationMonths = durationMonths;
	}

	public Long getId() {
		return id;
	}

	public AnonymousProfile getProfile() {
		return profile;
	}

	void setProfile(AnonymousProfile profile) {
		this.profile = profile;
	}

	public String getRoleTitle() {
		return roleTitle;
	}

	public int getDurationMonths() {
		return durationMonths;
	}
}
