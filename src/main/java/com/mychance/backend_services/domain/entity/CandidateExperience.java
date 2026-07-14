package com.mychance.backend_services.domain.entity;

import com.mychance.backend_services.domain.enums.SeniorityLevel;
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

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "seniority_level", nullable = false, length = 50)
	private SeniorityLevel seniorityLevel;

	@Column(name = "start_month", nullable = false)
	private int startMonth;

	@Column(name = "start_year", nullable = false)
	private int startYear;

	@Column(name = "end_month")
	private Integer endMonth;

	@Column(name = "end_year")
	private Integer endYear;

	@Column(name = "is_current", nullable = false)
	private boolean current;

	protected CandidateExperience() {
	}

	public CandidateExperience(
			String roleTitle,
			SeniorityLevel seniorityLevel,
			int startMonth,
			int startYear,
			Integer endMonth,
			Integer endYear,
			boolean current
	) {
		this.roleTitle = roleTitle;
		this.seniorityLevel = seniorityLevel;
		this.startMonth = startMonth;
		this.startYear = startYear;
		this.endMonth = endMonth;
		this.endYear = endYear;
		this.current = current;
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

	public SeniorityLevel getSeniorityLevel() {
		return seniorityLevel;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public int getStartYear() {
		return startYear;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public boolean isCurrent() {
		return current;
	}

	public YearMonth getStartYearMonth() {
		return YearMonth.of(startYear, startMonth);
	}

	public YearMonth getEndYearMonth() {
		if (current || endYear == null || endMonth == null) {
			return YearMonth.now();
		}
		return YearMonth.of(endYear, endMonth);
	}

	public int getDurationMonths() {
		long months = ChronoUnit.MONTHS.between(getStartYearMonth(), getEndYearMonth()) + 1;
		return (int) Math.max(1, months);
	}
}
