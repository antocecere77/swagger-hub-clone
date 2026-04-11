package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.dto.ApiVersionRequest;
import com.swaggerhub.clone.dto.ApiVersionResponse;
import com.swaggerhub.clone.service.ApiVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apis/{apiId}/versions")
@RequiredArgsConstructor
public class ApiVersionController {

    private final ApiVersionService apiVersionService;

    @GetMapping
    public ResponseEntity<List<ApiVersionResponse>> getVersionsByApiId(@PathVariable Long apiId) {
        List<ApiVersionResponse> responses = apiVersionService.getVersionsByApiId(apiId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<ApiVersionResponse> getVersionById(
            @PathVariable Long apiId,
            @PathVariable Long versionId) {
        ApiVersionResponse response = apiVersionService.getVersionById(apiId, versionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiVersionResponse> createVersion(
            @PathVariable Long apiId,
            @RequestBody @Valid ApiVersionRequest request) {
        ApiVersionResponse response = apiVersionService.createVersion(apiId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{versionId}")
    public ResponseEntity<ApiVersionResponse> updateVersion(
            @PathVariable Long apiId,
            @PathVariable Long versionId,
            @RequestBody @Valid ApiVersionRequest request) {
        ApiVersionResponse response = apiVersionService.updateVersion(apiId, versionId, request);
        return ResponseEntity.ok(response);
    }
}
