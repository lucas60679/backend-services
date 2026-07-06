package com.mychance.backend_services.repository;

import com.mychance.backend_services.domain.entity.InterviewInvite;
import com.mychance.backend_services.domain.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InterviewInviteRepository extends JpaRepository<InterviewInvite, UUID> {

	@Query("""
			SELECT invite FROM InterviewInvite invite
			JOIN FETCH invite.jobVacancy job
			JOIN FETCH invite.profile profile
			WHERE profile.id = :profileId
			ORDER BY invite.createdAt DESC
			""")
	List<InterviewInvite> findByProfileId(@Param("profileId") UUID profileId);

	@Query("""
			SELECT invite FROM InterviewInvite invite
			JOIN FETCH invite.jobVacancy job
			JOIN FETCH invite.profile profile
			WHERE job.id = :jobId
			ORDER BY invite.createdAt DESC
			""")
	List<InterviewInvite> findByJobId(@Param("jobId") UUID jobId);

	List<InterviewInvite> findByProfileIdAndStatusIn(UUID profileId, List<InviteStatus> statuses);
}
