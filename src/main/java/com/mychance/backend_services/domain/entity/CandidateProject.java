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
@Table(name = "candidate_projects")
public class CandidateProject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private AnonymousProfile profile;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	protected CandidateProject() {
	}

	public CandidateProject(String description) {
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
