package com.swaggerhub.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swaggerhub.clone.dto.ApiDefinitionRequest;
import com.swaggerhub.clone.model.ApiVisibility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetApis_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/apis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.totalPages", greaterThanOrEqualTo(0)));
    }

    @Test
    void testCreateApi_returnsCreated() throws Exception {
        ApiDefinitionRequest request = ApiDefinitionRequest.builder()
                .name("Test API")
                .description("Test Description")
                .category("Test")
                .visibility(ApiVisibility.PUBLIC)
                .tags("test,sample")
                .build();

        mockMvc.perform(post("/api/v1/apis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("Test API")))
                .andExpect(jsonPath("$.description", equalTo("Test Description")));
    }

    @Test
    void testGetApiById_returnsOk() throws Exception {
        ApiDefinitionRequest createRequest = ApiDefinitionRequest.builder()
                .name("Get Test API")
                .description("Test Description")
                .category("Test")
                .visibility(ApiVisibility.PUBLIC)
                .build();

        String createResponse = mockMvc.perform(post("/api/v1/apis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long apiId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/apis/" + apiId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(apiId.intValue())))
                .andExpect(jsonPath("$.name", equalTo("Get Test API")));
    }

    @Test
    void testDeleteApi_returnsNoContent() throws Exception {
        ApiDefinitionRequest createRequest = ApiDefinitionRequest.builder()
                .name("Delete Test API")
                .description("Test Description")
                .category("Test")
                .visibility(ApiVisibility.PUBLIC)
                .build();

        String createResponse = mockMvc.perform(post("/api/v1/apis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long apiId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/api/v1/apis/" + apiId))
                .andExpect(status().isNoContent());
    }
}
