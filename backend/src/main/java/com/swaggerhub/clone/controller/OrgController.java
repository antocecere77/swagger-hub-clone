package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.model.dto.request.CreateOrgRequest;
import com.swaggerhub.clone.model.dto.response.OrgResponse;
import com.swaggerhub.clone.model.entity.OrgMember;
import com.swaggerhub.clone.service.OrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orgs")
@RequiredArgsConstructor
@Tag(name = "Organizations", description = "Manage organizations and members")
@SecurityRequirement(name = "bearerAuth")
public class OrgController {

    private final OrgService orgService;

    @PostMapping
    @Operation(summary = "Create a new organization")
    public ResponseEntity<OrgResponse> createOrg(
            @Valid @RequestBody CreateOrgRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orgService.createOrg(request, userDetails.getUsername()));
    }

    @GetMapping("/me")
    @Operation(summary = "Get my organizations")
    public ResponseEntity<List<OrgResponse>> getMyOrgs(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orgService.getMyOrgs(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<OrgResponse> getOrg(@PathVariable UUID id) {
        return ResponseEntity.ok(orgService.getOrg(id));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get organization by slug")
    public ResponseEntity<OrgResponse> getOrgBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(orgService.getOrgBySlug(slug));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update organization")
    public ResponseEntity<OrgResponse> updateOrg(
            @PathVariable UUID id,
            @Valid @RequestBody CreateOrgRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orgService.updateOrg(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete organization")
    public ResponseEntity<Void> deleteOrg(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        orgService.deleteOrg(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Add a member to the organization")
    public ResponseEntity<OrgResponse> addMember(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = body.get("email");
        String roleStr = body.getOrDefault("role", "MEMBER");
        OrgMember.MemberRole role = OrgMember.MemberRole.valueOf(roleStr.toUpperCase());
        return ResponseEntity.ok(orgService.addMember(id, email, role, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "Remove a member from the organization")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        orgService.removeMember(id, userId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
