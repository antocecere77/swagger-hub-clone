package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.VersionStatus;
import java.time.LocalDateTime;

public record ApiVersionResponse(
    Long id,
    Long apiDefinitionId,
    String apiDefinitionName,
    String versionNumber,
    String spec,
    String specFormat,
    VersionStatus status,
    String changelog,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
