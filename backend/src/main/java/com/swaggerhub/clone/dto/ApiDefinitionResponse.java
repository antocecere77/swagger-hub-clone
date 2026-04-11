package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.ApiStatus;
import com.swaggerhub.clone.model.ApiVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDefinitionResponse {

    private Long id;

    private String name;

    private String description;

    private String category;

    private ApiVisibility visibility;

    private ApiStatus status;

    private String ownerId;

    private String tags;

    private Long versionsCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
