package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.ApiVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDefinitionRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String category;

    private ApiVisibility visibility;

    private String tags;
}
