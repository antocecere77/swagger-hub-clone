package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.ApiDefinition;
import com.swaggerhub.clone.model.ApiStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDefinitionRepository extends JpaRepository<ApiDefinition, Long> {

    Page<ApiDefinition> findByStatusNot(ApiStatus status, Pageable pageable);

    Page<ApiDefinition> findByStatusNotAndNameContainingIgnoreCase(ApiStatus status, String name, Pageable pageable);

    long countByStatus(ApiStatus status);
}
