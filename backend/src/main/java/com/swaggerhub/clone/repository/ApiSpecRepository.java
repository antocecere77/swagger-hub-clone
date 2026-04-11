package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.entity.ApiSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiSpecRepository extends JpaRepository<ApiSpec, UUID> {

    Page<ApiSpec> findByOwnerId(UUID ownerId, Pageable pageable);

    Page<ApiSpec> findByOrganizationId(UUID orgId, Pageable pageable);

    Page<ApiSpec> findByVisibility(ApiSpec.Visibility visibility, Pageable pageable);

    @Query("SELECT s FROM ApiSpec s WHERE s.visibility = 'PUBLIC' AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<ApiSpec> searchPublic(@Param("query") String query, Pageable pageable);

    @Query("SELECT s FROM ApiSpec s WHERE (s.owner.id = :userId OR s.visibility = 'PUBLIC') AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<ApiSpec> searchForUser(@Param("query") String query, @Param("userId") UUID userId, Pageable pageable);

    Page<ApiSpec> findByOwnerIdOrVisibility(UUID ownerId, ApiSpec.Visibility visibility, Pageable pageable);
}
