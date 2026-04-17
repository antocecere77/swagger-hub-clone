package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.ApiVersion;
import com.swaggerhub.clone.model.VersionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long> {

    List<ApiVersion> findByApiDefinitionIdOrderByCreatedAtDesc(Long apiDefinitionId);

    Optional<ApiVersion> findByApiDefinitionIdAndId(Long apiDefinitionId, Long versionId);

    long countByApiDefinitionId(Long apiDefinitionId);

    boolean existsByApiDefinitionIdAndVersionNumber(Long apiDefinitionId, String versionNumber);

    List<ApiVersion> findByApiDefinitionIdAndStatus(Long apiDefinitionId, VersionStatus status);
}
