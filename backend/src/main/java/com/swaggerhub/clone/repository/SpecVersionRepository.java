package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.entity.SpecVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecVersionRepository extends JpaRepository<SpecVersion, UUID> {

    List<SpecVersion> findBySpecIdOrderByCreatedAtDesc(UUID specId);

    Optional<SpecVersion> findBySpecIdAndVersionNumber(UUID specId, String versionNumber);

    Optional<SpecVersion> findTopBySpecIdOrderByCreatedAtDesc(UUID specId);
}
