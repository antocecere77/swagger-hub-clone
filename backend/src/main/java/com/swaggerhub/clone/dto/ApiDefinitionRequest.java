package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.ApiVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApiDefinitionRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
    String name,

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    String description,

    @Size(max = 100, message = "Category must be at most 100 characters")
    String category,

    ApiVisibility visibility,

    @Size(max = 500, message = "Tags must be at most 500 characters")
    String tags
) {}
