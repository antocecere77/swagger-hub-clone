package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.ApiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiVersionRepository extends JpaRepository<ApiVersion, Long> {

    List<ApiVersion> findByApiDefinitionIdOrderByCreatedAtDesc(Long apiDefinitionId);

    long countByApiDefinitionId(Long apiDefinitionId);
}
