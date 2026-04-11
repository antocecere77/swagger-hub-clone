package com.swaggerhub.clone.dto;

import com.swaggerhub.clone.model.VersionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiVersionResponse {

    private Long id;

    private Long apiDefinitionId;

    private String versionNumber;

    private String spec;

    private String specFormat;

    private VersionStatus status;

    private String changelog;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
