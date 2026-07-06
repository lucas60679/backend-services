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

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "max_salary")
	private Integer maxSalary;

	@OneToMany(mappedBy = "jobVacancy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobRequirement> requirements = new ArrayList<>();

	protected JobVacancy() {
	}

	public JobVacancy(String title, UUID recruiterId) {
		this.title = title;
		this.recruiterId = recruiterId;
	}

	public JobVacancy(String title, UUID recruiterId, String description, Integer maxSalary) {
		this.title = title;
		this.recruiterId = recruiterId;
		this.description = description;
		this.maxSalary = maxSalary;
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

	public String getDescription() {
		return description;
	}

	public Integer getMaxSalary() {
		return maxSalary;
	}

	public List<JobRequirement> getRequirements() {
		return requirements;
	}

	public void addRequirement(JobRequirement requirement) {
		requirements.add(requirement);
		requirement.setJobVacancy(this);
	}
}
