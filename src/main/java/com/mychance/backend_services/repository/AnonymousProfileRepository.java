package com.mychance.backend_services.repository;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AnonymousProfileRepository extends JpaRepository<AnonymousProfile, UUID> {

	@Query(value = """
			SELECT * FROM anonymous_profiles
			WHERE SUBSTRING(REPLACE(CAST(id AS VARCHAR(36)), '-', ''), 1, 8) = :prefix
			""", nativeQuery = true)
	Optional<AnonymousProfile> findByPublicIdPrefix(@Param("prefix") String prefix);

	@Query("""
			SELECT profile FROM AnonymousProfile profile
			WHERE profile.candidate.id = :candidateId
			""")
	Optional<AnonymousProfile> findByCandidateId(@Param("candidateId") UUID candidateId);

	@Query("""
			SELECT profile FROM AnonymousProfile profile
			LEFT JOIN FETCH profile.skills
			LEFT JOIN FETCH profile.experiences
			LEFT JOIN FETCH profile.projects
			WHERE profile.id = :profileId
			""")
	Optional<AnonymousProfile> findByIdWithDetails(@Param("profileId") UUID profileId);
}
