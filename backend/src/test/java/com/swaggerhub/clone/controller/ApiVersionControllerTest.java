package com.swaggerhub.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaggerhub.clone.dto.ApiVersionRequest;
import com.swaggerhub.clone.model.VersionStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @Transactional: contesto caricato una volta sola, rollback automatico dopo ogni test.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("ApiVersionController Integration Tests")
class ApiVersionControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/apis/{apiId}/versions returns list")
    void getVersions_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis/1/versions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET versions for non-existing API returns 404")
    void getVersions_apiNotFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/apis/9999/versions"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/apis/{apiId}/versions creates version and returns 201")
    void createVersion_returnsCreated() throws Exception {
        ApiVersionRequest request = new ApiVersionRequest(
            "2.0.0",
            "openapi: '3.0.0'\ninfo:\n  title: Test\n  version: 2.0.0",
            "YAML", VersionStatus.DRAFT, "New major version"
        );
        mockMvc.perform(post("/api/v1/apis/1/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.versionNumber").value("2.0.0"))
            .andExpect(jsonPath("$.status").value("DRAFT"))
            .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("POST duplicate version returns 409 Conflict")
    void createVersion_duplicate_returns409() throws Exception {
        ApiVersionRequest request = new ApiVersionRequest(
            "1.0.0", null, "YAML", VersionStatus.DRAFT, null
        );
        mockMvc.perform(post("/api/v1/apis/1/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST version with invalid semver returns 400")
    void createVersion_invalidSemver_returns400() throws Exception {
        ApiVersionRequest request = new ApiVersionRequest(
            "v1-bad", null, "YAML", VersionStatus.DRAFT, null
        );
        mockMvc.perform(post("/api/v1/apis/1/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/apis/{apiId}/versions/{versionId} returns version with spec")
    void getVersion_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis/1/versions/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.versionNumber").isString())
            .andExpect(jsonPath("$.spec").isString());
    }

    @Test
    @DisplayName("GET non-existing version returns 404")
    void getVersion_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/apis/1/versions/9999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/apis/{apiId}/versions/{versionId} updates status to PUBLISHED")
    void updateVersion_returnsOk() throws Exception {
        ApiVersionRequest request = new ApiVersionRequest(
            "1.0.0", null, "YAML", VersionStatus.PUBLISHED, "Published to production"
        );
        mockMvc.perform(put("/api/v1/apis/1/versions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }
}
