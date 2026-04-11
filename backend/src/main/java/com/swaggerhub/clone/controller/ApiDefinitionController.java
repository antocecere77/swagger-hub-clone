package com.swaggerhub.clone.controller;

import com.swaggerhub.clone.dto.ApiDefinitionRequest;
import com.swaggerhub.clone.dto.ApiDefinitionResponse;
import com.swaggerhub.clone.dto.PageResponse;
import com.swaggerhub.clone.service.ApiDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/apis")
@RequiredArgsConstructor
public class ApiDefinitionController {

    private final ApiDefinitionService apiDefinitionService;

    @GetMapping
    public ResponseEntity<PageResponse<ApiDefinitionResponse>> getApis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        PageResponse<ApiDefinitionResponse> response = apiDefinitionService.getApis(page, size, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDefinitionResponse> getApiById(@PathVariable Long id) {
        ApiDefinitionResponse response = apiDefinitionService.getApiById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiDefinitionResponse> createApi(
            @RequestBody @Valid ApiDefinitionRequest request) {
        ApiDefinitionResponse response = apiDefinitionService.createApi(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDefinitionResponse> updateApi(
            @PathVariable Long id,
            @RequestBody @Valid ApiDefinitionRequest request) {
        ApiDefinitionResponse response = apiDefinitionService.updateApi(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApi(@PathVariable Long id) {
        apiDefinitionService.deleteApi(id);
        return ResponseEntity.noContent().build();
    }
}
