package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.VersionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiVersionRequest {

    @NotBlank(message = "Version number is required")
    private String versionNumber;

    @NotBlank(message = "Spec is required")
    private String spec;

    @NotBlank(message = "Spec format is required")
    private String specFormat;

    private String changelog;

    private VersionStatus status;
}
