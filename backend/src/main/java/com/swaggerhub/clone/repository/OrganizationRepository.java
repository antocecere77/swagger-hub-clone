package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Organization> findByOwnerId(UUID ownerId);

    @Query("SELECT o FROM Organization o JOIN o.members m WHERE m.user.id = :userId")
    List<Organization> findByMemberUserId(@Param("userId") UUID userId);
}
