package com.mychance.backend_services.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "anonymous_profiles")
public class AnonymousProfile {

	@Id
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "candidate_id", nullable = false, unique = true)
	private Candidate candidate;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CandidateSkill> skills = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CandidateProject> projects = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CandidateExperience> experiences = new ArrayList<>();

	protected AnonymousProfile() {
	}

	public AnonymousProfile(Candidate candidate) {
		this.candidate = candidate;
	}

	@PrePersist
	void onCreate() {
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

	public Candidate getCandidate() {
		return candidate;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public List<CandidateSkill> getSkills() {
		return skills;
	}

	public List<CandidateProject> getProjects() {
		return projects;
	}

	public List<CandidateExperience> getExperiences() {
		return experiences;
	}

	public void addSkill(CandidateSkill skill) {
		skills.add(skill);
		skill.setProfile(this);
	}

	public void addProject(CandidateProject project) {
		projects.add(project);
		project.setProfile(this);
	}

	public void addExperience(CandidateExperience experience) {
		experiences.add(experience);
		experience.setProfile(this);
	}
}
