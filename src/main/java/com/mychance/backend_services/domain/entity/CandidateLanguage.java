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
@Table(name = "candidate_languages")
public class CandidateLanguage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private AnonymousProfile profile;

	@Enumerated(EnumType.STRING)
	@Column(name = "language_name", nullable = false, length = 50)
	private LanguageName languageName;

	@Enumerated(EnumType.STRING)
	@Column(name = "language_level", nullable = false, length = 50)
	private LanguageLevel languageLevel;

	protected CandidateLanguage() {
	}

	public CandidateLanguage(LanguageName languageName, LanguageLevel languageLevel) {
		this.languageName = languageName;
		this.languageLevel = languageLevel;
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

	public LanguageName getLanguageName() {
		return languageName;
	}

	public LanguageLevel getLanguageLevel() {
		return languageLevel;
	}
}
