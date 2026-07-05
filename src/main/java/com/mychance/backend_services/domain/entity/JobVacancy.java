package com.mychance.backend_services.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_vacancies")
public class JobVacancy {

	@Id
	private UUID id;

	@Column(nullable = false, length = 255)
	private String title;

	@Column(name = "recruiter_id", nullable = false)
	private UUID recruiterId;

	@OneToMany(mappedBy = "jobVacancy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobRequirement> requirements = new ArrayList<>();

	protected JobVacancy() {
	}

	public JobVacancy(String title, UUID recruiterId) {
		this.title = title;
		this.recruiterId = recruiterId;
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

	public String getTitle() {
		return title;
	}

	public UUID getRecruiterId() {
		return recruiterId;
	}

	public List<JobRequirement> getRequirements() {
		return requirements;
	}

	public void addRequirement(JobRequirement requirement) {
		requirements.add(requirement);
		requirement.setJobVacancy(this);
	}
}
