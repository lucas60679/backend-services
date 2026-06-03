package com.mychance.backend_services.domain.entity;

import com.mychance.backend_services.domain.enums.SkillName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_requirements")
public class JobRequirement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_id", nullable = false)
	private JobVacancy jobVacancy;

	@Enumerated(EnumType.STRING)
	@Column(name = "skill_name", nullable = false, length = 100)
	private SkillName skillName;

	@Column(nullable = false)
	private int weight;

	@Column(name = "is_mandatory", nullable = false)
	private boolean mandatory;

	@Column(name = "min_level", nullable = false)
	private int minLevel;

	protected JobRequirement() {
	}

	public JobRequirement(SkillName skillName, int weight, boolean mandatory, int minLevel) {
		this.skillName = skillName;
		this.weight = weight;
		this.mandatory = mandatory;
		this.minLevel = minLevel;
	}

	public Long getId() {
		return id;
	}

	public JobVacancy getJobVacancy() {
		return jobVacancy;
	}

	void setJobVacancy(JobVacancy jobVacancy) {
		this.jobVacancy = jobVacancy;
	}

	public SkillName getSkillName() {
		return skillName;
	}

	public int getWeight() {
		return weight;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public int getMinLevel() {
		return minLevel;
	}
}
