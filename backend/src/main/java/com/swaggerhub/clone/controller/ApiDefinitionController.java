package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.dto.*;
import com.swaggerhub.clone.service.DashboardService;
import com.swaggerhub.clone.service.ApiDefinitionService;
import com.swaggerhub.clone.service.DashboardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/apis")
@RequiredArgsConstructor
@Tag(name = "API Definitions", description = "Manage API definitions")
public class ApiDefinitionController {

    private final ApiDefinitionService apiService;
    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "List all APIs with pagination and filtering")
    public ResponseEntity<PageResponse<ApiDefinitionResponse>> getApis(
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "")   String search,
        @RequestParam(defaultValue = "")   String category,
        @RequestParam(defaultValue = "updatedAt") String sort
    ) {
        log.info("GET /api/v1/apis - page={}, size={}, search='{}', category='{}'", page, size, search, category);
        return ResponseEntity.ok(apiService.getApis(page, size, search, category, sort));
    }

    @PostMapping
    @Operation(summary = "Create a new API definition")
    public ResponseEntity<ApiDefinitionResponse> createApi(@Valid @RequestBody ApiDefinitionRequest request) {
        log.info("POST /api/v1/apis - name='{}'", request.name());
        ApiDefinitionResponse created = apiService.createApi(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an API definition by ID")
    public ResponseEntity<ApiDefinitionResponse> getApiById(@PathVariable Long id) {
        log.info("GET /api/v1/apis/{}", id);
        return ResponseEntity.ok(apiService.getApiById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an API definition")
    public ResponseEntity<ApiDefinitionResponse> updateApi(
        @PathVariable Long id,
        @Valid @RequestBody ApiDefinitionRequest request
    ) {
        log.info("PUT /api/v1/apis/{} - name='{}'", id, request.name());
        return ResponseEntity.ok(apiService.updateApi(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an API definition")
    public ResponseEntity<Void> deleteApi(@PathVariable Long id) {
        log.info("DELETE /api/v1/apis/{}", id);
        apiService.deleteApi(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<DashboardServiceImpl.DashboardStats> getStats() {
        log.info("GET /api/v1/apis/stats");
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
