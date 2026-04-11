package com.swaggerhub.clone.model.dto.response;

import com.swaggerhub.clone.model.entity.ApiSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecResponse {

    private UUID id;
    private String title;
    private String description;
    private String version;
    private UUID orgId;
    private String orgName;
    private UserResponse owner;
    private ApiSpec.Visibility visibility;
    private List<String> tags;
    private int versionsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SpecResponse from(ApiSpec spec) {
        return SpecResponse.builder()
                .id(spec.getId())
                .title(spec.getTitle())
                .description(spec.getDescription())
                .version(spec.getVersion())
                .orgId(spec.getOrganization() != null ? spec.getOrganization().getId() : null)
                .orgName(spec.getOrganization() != null ? spec.getOrganization().getName() : null)
                .owner(UserResponse.from(spec.getOwner()))
                .visibility(spec.getVisibility())
                .tags(spec.getTags().stream().map(t -> t.getTag()).collect(Collectors.toList()))
                .versionsCount(spec.getVersions().size())
                .createdAt(spec.getCreatedAt())
                .updatedAt(spec.getUpdatedAt())
                .build();
    }
}
