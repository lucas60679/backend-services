package com.mychance.backend_services.domain.entity;

import com.mychance.backend_services.domain.enums.LanguageLevel;
import com.mychance.backend_services.domain.enums.LanguageName;
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
@Table(name = "job_language_requirements")
public class JobLanguageRequirement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_id", nullable = false)
	private JobVacancy jobVacancy;

	@Enumerated(EnumType.STRING)
	@Column(name = "language_name", nullable = false, length = 50)
	private LanguageName languageName;

	@Enumerated(EnumType.STRING)
	@Column(name = "min_level", nullable = false, length = 50)
	private LanguageLevel minLevel;

	protected JobLanguageRequirement() {
	}

	public JobLanguageRequirement(LanguageName languageName, LanguageLevel minLevel) {
		this.languageName = languageName;
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

	public LanguageName getLanguageName() {
		return languageName;
	}

	public LanguageLevel getMinLevel() {
		return minLevel;
	}
}
