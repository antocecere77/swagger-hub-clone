package com.swaggerhub.clone.model.dto.response;

import com.swaggerhub.clone.model.entity.SpecVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecVersionResponse {

    private UUID id;
    private UUID specId;
    private String versionNumber;
    private String contentYaml;
    private String contentJson;
    private String notes;
    private LocalDateTime createdAt;
    private UserResponse createdBy;

    public static SpecVersionResponse from(SpecVersion version) {
        return SpecVersionResponse.builder()
                .id(version.getId())
                .specId(version.getSpec().getId())
                .versionNumber(version.getVersionNumber())
                .contentYaml(version.getContentYaml())
                .contentJson(version.getContentJson())
                .notes(version.getNotes())
                .createdAt(version.getCreatedAt())
                .createdBy(UserResponse.from(version.getCreatedBy()))
                .build();
    }
}
