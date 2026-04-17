package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.ApiStatus;
import com.swaggerhub.clone.model.ApiVisibility;
import java.time.LocalDateTime;

public record ApiDefinitionResponse(
    Long id,
    String name,
    String description,
    String category,
    ApiVisibility visibility,
    ApiStatus status,
    String ownerId,
    String tags,
    long versionsCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
