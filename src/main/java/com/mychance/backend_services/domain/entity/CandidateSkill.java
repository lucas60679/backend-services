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

import java.util.UUID;

@Entity
@Table(name = "candidate_skills")
public class CandidateSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private AnonymousProfile profile;

	@Enumerated(EnumType.STRING)
	@Column(name = "skill_name", nullable = false, length = 100)
	private SkillName skillName;

	@Column(name = "skill_level", nullable = false)
	private int skillLevel;

	protected CandidateSkill() {
	}

	public CandidateSkill(SkillName skillName, int skillLevel) {
		this.skillName = skillName;
		this.skillLevel = skillLevel;
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

	public SkillName getSkillName() {
		return skillName;
	}

	public int getSkillLevel() {
		return skillLevel;
	}
}
