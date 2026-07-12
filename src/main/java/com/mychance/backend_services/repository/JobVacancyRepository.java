package com.mychance.backend_services.repository;

import com.mychance.backend_services.domain.entity.JobVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobVacancyRepository extends JpaRepository<JobVacancy, UUID> {

	@Query("""
			SELECT j FROM JobVacancy j
			LEFT JOIN FETCH j.requirements
			WHERE j.id = :id
			""")
	Optional<JobVacancy> findByIdWithRequirements(UUID id);

	List<JobVacancy> findByRecruiterIdOrderByTitleAsc(UUID recruiterId);
}
