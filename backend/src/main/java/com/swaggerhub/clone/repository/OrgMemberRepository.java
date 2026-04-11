package com.swaggerhub.clone.repository;

import com.swaggerhub.clone.model.entity.OrgMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgMemberRepository extends JpaRepository<OrgMember, OrgMember.OrgMemberId> {

    List<OrgMember> findByOrganizationId(UUID orgId);

    Optional<OrgMember> findByOrganizationIdAndUserId(UUID orgId, UUID userId);

    boolean existsByOrganizationIdAndUserId(UUID orgId, UUID userId);

    void deleteByOrganizationIdAndUserId(UUID orgId, UUID userId);
}
