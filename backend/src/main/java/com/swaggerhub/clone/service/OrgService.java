package com.swaggerhub.clone.service;

import com.swaggerhub.clone.exception.DuplicateResourceException;
import com.swaggerhub.clone.exception.ResourceNotFoundException;
import com.swaggerhub.clone.exception.UnauthorizedException;
import com.swaggerhub.clone.model.dto.request.CreateOrgRequest;
import com.swaggerhub.clone.model.dto.response.OrgResponse;
import com.swaggerhub.clone.model.entity.OrgMember;
import com.swaggerhub.clone.model.entity.Organization;
import com.swaggerhub.clone.model.entity.User;
import com.swaggerhub.clone.repository.OrgMemberRepository;
import com.swaggerhub.clone.repository.OrganizationRepository;
import com.swaggerhub.clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrgService {

    private final OrganizationRepository orgRepository;
    private final OrgMemberRepository memberRepository;
    private final UserRepository userRepository;

    public OrgResponse createOrg(CreateOrgRequest request, String userEmail) {
        if (orgRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Organization with slug '" + request.getSlug() + "' already exists");
        }

        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Organization org = Organization.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .website(request.getWebsite())
                .owner(owner)
                .build();

        org = orgRepository.save(org);

        // Add owner as OWNER member
        OrgMember ownerMember = OrgMember.builder()
                .id(new OrgMember.OrgMemberId(org.getId(), owner.getId()))
                .organization(org)
                .user(owner)
                .role(OrgMember.MemberRole.OWNER)
                .build();
        memberRepository.save(ownerMember);

        org = orgRepository.findById(org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Org not found after save"));

        log.info("Created organization: {} by {}", org.getSlug(), userEmail);
        return OrgResponse.from(org);
    }

    @Transactional(readOnly = true)
    public OrgResponse getOrg(UUID id) {
        Organization org = orgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        return OrgResponse.from(org);
    }

    @Transactional(readOnly = true)
    public OrgResponse getOrgBySlug(String slug) {
        Organization org = orgRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with slug: " + slug));
        return OrgResponse.from(org);
    }

    @Transactional(readOnly = true)
    public List<OrgResponse> getMyOrgs(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Organization> orgs = orgRepository.findByMemberUserId(user.getId());
        return orgs.stream().map(OrgResponse::from).collect(Collectors.toList());
    }

    public OrgResponse updateOrg(UUID id, CreateOrgRequest request, String userEmail) {
        Organization org = orgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        checkAdminAccess(org, userEmail);

        if (!org.getSlug().equals(request.getSlug()) && orgRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Organization with slug '" + request.getSlug() + "' already exists");
        }

        org.setName(request.getName());
        org.setSlug(request.getSlug());
        org.setDescription(request.getDescription());
        org.setWebsite(request.getWebsite());

        org = orgRepository.save(org);
        return OrgResponse.from(org);
    }

    public void deleteOrg(UUID id, String userEmail) {
        Organization org = orgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));

        checkOwnerAccess(org, userEmail);
        orgRepository.delete(org);
        log.info("Deleted organization: {}", id);
    }

    public OrgResponse addMember(UUID orgId, String memberEmail, OrgMember.MemberRole role, String requestorEmail) {
        Organization org = orgRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));

        checkAdminAccess(org, requestorEmail);

        User member = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + memberEmail));

        if (memberRepository.existsByOrganizationIdAndUserId(orgId, member.getId())) {
            throw new DuplicateResourceException("User is already a member of this organization");
        }

        OrgMember orgMember = OrgMember.builder()
                .id(new OrgMember.OrgMemberId(orgId, member.getId()))
                .organization(org)
                .user(member)
                .role(role)
                .build();

        memberRepository.save(orgMember);
        org = orgRepository.findById(orgId).orElseThrow();
        return OrgResponse.from(org);
    }

    public void removeMember(UUID orgId, UUID userId, String requestorEmail) {
        Organization org = orgRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));

        checkAdminAccess(org, requestorEmail);
        memberRepository.deleteByOrganizationIdAndUserId(orgId, userId);
        log.info("Removed member {} from org {}", userId, orgId);
    }

    private void checkAdminAccess(Organization org, String userEmail) {
        OrgMember member = memberRepository.findByOrganizationIdAndUserId(
                org.getId(),
                userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                        .getId()
        ).orElseThrow(() -> new UnauthorizedException("You are not a member of this organization"));

        if (member.getRole() == OrgMember.MemberRole.MEMBER) {
            throw new UnauthorizedException("Admin or Owner role required");
        }
    }

    private void checkOwnerAccess(Organization org, String userEmail) {
        if (!org.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("Only the owner can perform this action");
        }
    }
}
