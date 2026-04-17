package com.swaggerhub.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaggerhub.clone.dto.ApiDefinitionRequest;
import com.swaggerhub.clone.model.ApiVisibility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("ApiDefinitionController Integration Tests")
class ApiDefinitionControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/apis returns 200 with paginated list")
    void getApis_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.currentPage").value(0))
            .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/apis?search=Petstore returns filtered results")
    void getApis_withSearch_returnsFiltered() throws Exception {
        mockMvc.perform(get("/api/v1/apis").param("search", "Petstore"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Petstore API"));
    }

    @Test
    @DisplayName("GET /api/v1/apis?category=Finance returns category results")
    void getApis_withCategory_returnsFiltered() throws Exception {
        mockMvc.perform(get("/api/v1/apis").param("category", "Finance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].category").value("Finance"));
    }

    @Test
    @DisplayName("POST /api/v1/apis creates API and returns 201 with Location header")
    void createApi_returnsCreated() throws Exception {
        ApiDefinitionRequest request = new ApiDefinitionRequest(
            "Test API", "A test API", "Testing", ApiVisibility.PUBLIC, "test,api"
        );
        mockMvc.perform(post("/api/v1/apis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("Test API"))
            .andExpect(jsonPath("$.category").value("Testing"))
            .andExpect(jsonPath("$.visibility").value("PUBLIC"))
            .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("POST /api/v1/apis with blank name returns 400")
    void createApi_blankName_returns400() throws Exception {
        ApiDefinitionRequest request = new ApiDefinitionRequest("", null, null, null, null);
        mockMvc.perform(post("/api/v1/apis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").exists());
    }

    @Test
    @DisplayName("GET /api/v1/apis/{id} returns 200 for existing API")
    void getApiById_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").isString());
    }

    @Test
    @DisplayName("GET /api/v1/apis/{id} returns 404 for missing API")
    void getApiById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/apis/9999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/apis/{id} updates name and returns 200")
    void updateApi_returnsOk() throws Exception {
        ApiDefinitionRequest request = new ApiDefinitionRequest(
            "Updated Name", "Updated desc", "Finance", ApiVisibility.PRIVATE, null
        );
        mockMvc.perform(put("/api/v1/apis/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Name"))
            .andExpect(jsonPath("$.visibility").value("PRIVATE"));
    }

    @Test
    @DisplayName("DELETE /api/v1/apis/{id} soft-deletes and returns 204")
    void deleteApi_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/apis/1"))
            .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/apis/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/apis/stats returns dashboard stats")
    void getStats_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalApis").isNumber())
            .andExpect(jsonPath("$.publishedVersions").isNumber())
            .andExpect(jsonPath("$.draftVersions").isNumber());
    }
}
