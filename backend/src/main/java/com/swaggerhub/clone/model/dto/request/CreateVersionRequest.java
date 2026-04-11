package com.swaggerhub.clone.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateVersionRequest {

    @NotBlank(message = "Version number is required")
    @Size(max = 100, message = "Version number must not exceed 100 characters")
    private String versionNumber;

    @NotBlank(message = "Content YAML is required")
    private String contentYaml;

    private String notes;
}
