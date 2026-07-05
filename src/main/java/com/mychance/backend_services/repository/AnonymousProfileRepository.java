package com.mychance.backend_services.repository;

import com.mychance.backend_services.domain.entity.AnonymousProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnonymousProfileRepository extends JpaRepository<AnonymousProfile, UUID> {
}
