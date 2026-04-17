package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.dto.*;
import com.swaggerhub.clone.service.ApiVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/apis/{apiId}/versions")
@RequiredArgsConstructor
@Tag(name = "API Versions", description = "Manage API versions and specs")
public class ApiVersionController {

    private final ApiVersionService versionService;

    @GetMapping
    @Operation(summary = "List all versions of an API")
    public ResponseEntity<List<ApiVersionResponse>> getVersions(@PathVariable Long apiId) {
        log.info("GET /api/v1/apis/{}/versions", apiId);
        return ResponseEntity.ok(versionService.getVersionsByApiId(apiId));
    }

    @PostMapping
    @Operation(summary = "Create a new version for an API")
    public ResponseEntity<ApiVersionResponse> createVersion(
        @PathVariable Long apiId,
        @Valid @RequestBody ApiVersionRequest request
    ) {
        log.info("POST /api/v1/apis/{}/versions - version='{}'", apiId, request.versionNumber());
        ApiVersionResponse created = versionService.createVersion(apiId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{versionId}")
    @Operation(summary = "Get a specific version of an API")
    public ResponseEntity<ApiVersionResponse> getVersion(
        @PathVariable Long apiId,
        @PathVariable Long versionId
    ) {
        log.info("GET /api/v1/apis/{}/versions/{}", apiId, versionId);
        return ResponseEntity.ok(versionService.getVersionById(apiId, versionId));
    }

    @PutMapping("/{versionId}")
    @Operation(summary = "Update a version spec, status or changelog")
    public ResponseEntity<ApiVersionResponse> updateVersion(
        @PathVariable Long apiId,
        @PathVariable Long versionId,
        @Valid @RequestBody ApiVersionRequest request
    ) {
        log.info("PUT /api/v1/apis/{}/versions/{}", apiId, versionId);
        return ResponseEntity.ok(versionService.updateVersion(apiId, versionId, request));
    }
}
