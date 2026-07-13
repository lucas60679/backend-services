package com.mychance.backend_services.domain.entity;

import com.mychance.backend_services.domain.enums.InviteStatus;
import com.mychance.backend_services.domain.enums.ScheduleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "interview_invites")
public class InterviewInvite {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_id", nullable = false)
	private JobVacancy jobVacancy;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private AnonymousProfile profile;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private InviteStatus status;

	@Column(length = 500)
	private String message;

	@Column(name = "proposed_interview_at")
	private Instant proposedInterviewAt;

	@Column(name = "meeting_link", length = 500)
	private String meetingLink;

	@Enumerated(EnumType.STRING)
	@Column(name = "schedule_status", length = 30)
	private ScheduleStatus scheduleStatus;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected InterviewInvite() {
	}

	public InterviewInvite(JobVacancy jobVacancy, AnonymousProfile profile, InviteStatus status, String message) {
		this.jobVacancy = jobVacancy;
		this.profile = profile;
		this.status = status;
		this.message = message;
	}

	@PrePersist
	void onCreate() {
		if (id == null) {
			id = UUID.randomUUID();
		}
		Instant now = Instant.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public JobVacancy getJobVacancy() {
		return jobVacancy;
	}

	public AnonymousProfile getProfile() {
		return profile;
	}

	public InviteStatus getStatus() {
		return status;
	}

	public void setStatus(InviteStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public Instant getProposedInterviewAt() {
		return proposedInterviewAt;
	}

	public void setProposedInterviewAt(Instant proposedInterviewAt) {
		this.proposedInterviewAt = proposedInterviewAt;
	}

	public String getMeetingLink() {
		return meetingLink;
	}

	public void setMeetingLink(String meetingLink) {
		this.meetingLink = meetingLink;
	}

	public ScheduleStatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
