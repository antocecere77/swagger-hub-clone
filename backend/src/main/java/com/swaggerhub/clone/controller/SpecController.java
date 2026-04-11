package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.model.dto.request.CreateSpecRequest;
import com.swaggerhub.clone.model.dto.request.CreateVersionRequest;
import com.swaggerhub.clone.model.dto.request.UpdateSpecRequest;
import com.swaggerhub.clone.model.dto.response.PagedResponse;
import com.swaggerhub.clone.model.dto.response.SpecResponse;
import com.swaggerhub.clone.model.dto.response.SpecVersionResponse;
import com.swaggerhub.clone.service.SpecService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/specs")
@RequiredArgsConstructor
@Tag(name = "API Specifications", description = "Manage API specifications and versions")
@SecurityRequirement(name = "bearerAuth")
public class SpecController {

    private final SpecService specService;

    @PostMapping
    @Operation(summary = "Create a new API specification")
    public ResponseEntity<SpecResponse> createSpec(
            @Valid @RequestBody CreateSpecRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specService.createSpec(request, userDetails.getUsername()));
    }

    @GetMapping("/me")
    @Operation(summary = "Get my API specifications")
    public ResponseEntity<PagedResponse<SpecResponse>> getMySpecs(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(specService.getMySpecs(userDetails.getUsername(), page, size));
    }

    @GetMapping("/public")
    @Operation(summary = "Browse public API specifications")
    public ResponseEntity<PagedResponse<SpecResponse>> getPublicSpecs(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(specService.getPublicSpecs(query, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get API specification by ID")
    public ResponseEntity<SpecResponse> getSpec(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(specService.getSpec(id, email));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an API specification")
    public ResponseEntity<SpecResponse> updateSpec(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSpecRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(specService.updateSpec(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an API specification")
    public ResponseEntity<Void> deleteSpec(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        specService.deleteSpec(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/versions")
    @Operation(summary = "Create a new version of an API specification")
    public ResponseEntity<SpecVersionResponse> createVersion(
            @PathVariable UUID id,
            @Valid @RequestBody CreateVersionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specService.createVersion(id, request, userDetails.getUsername()));
    }

    @GetMapping("/{id}/versions")
    @Operation(summary = "Get all versions of an API specification")
    public ResponseEntity<List<SpecVersionResponse>> getVersions(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(specService.getVersions(id, email));
    }

    @GetMapping("/{id}/versions/latest")
    @Operation(summary = "Get the latest version of an API specification")
    public ResponseEntity<SpecVersionResponse> getLatestVersion(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(specService.getLatestVersion(id, email));
    }

    @GetMapping("/{id}/versions/{versionId}")
    @Operation(summary = "Get a specific version of an API specification")
    public ResponseEntity<SpecVersionResponse> getVersion(
            @PathVariable UUID id,
            @PathVariable UUID versionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(specService.getVersion(id, versionId, email));
    }
}
