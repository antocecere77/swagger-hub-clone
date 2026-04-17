package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.VersionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ApiVersionRequest(
    @NotBlank(message = "Version number is required")
    @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "Version must follow semver format (e.g. 1.0.0)")
    String versionNumber,

    String spec,

    @Pattern(regexp = "^(YAML|JSON)$", message = "Format must be YAML or JSON")
    String specFormat,

    VersionStatus status,

    String changelog
) {}
