package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.ApiVersionRequest;
import com.swaggerhub.clone.dto.ApiVersionResponse;
import com.swaggerhub.clone.model.ApiDefinition;
import com.swaggerhub.clone.model.ApiVersion;
import com.swaggerhub.clone.model.VersionStatus;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiVersionService {

    private final ApiVersionRepository apiVersionRepository;
    private final ApiDefinitionRepository apiDefinitionRepository;

    public List<ApiVersionResponse> getVersionsByApiId(Long apiId) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(apiId)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + apiId));

        return apiVersionRepository.findByApiDefinitionIdOrderByCreatedAtDesc(apiId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApiVersionResponse getVersionById(Long apiId, Long versionId) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(apiId)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + apiId));

        ApiVersion apiVersion = apiVersionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("API Version not found with id: " + versionId));

        if (!apiVersion.getApiDefinition().getId().equals(apiId)) {
            throw new EntityNotFoundException("API Version not found with id: " + versionId);
        }

        return mapToResponse(apiVersion);
    }

    public ApiVersionResponse createVersion(Long apiId, ApiVersionRequest request) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(apiId)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + apiId));

        ApiVersion apiVersion = ApiVersion.builder()
                .apiDefinition(apiDefinition)
                .versionNumber(request.getVersionNumber())
                .spec(request.getSpec())
                .specFormat(request.getSpecFormat())
                .status(request.getStatus() != null ? request.getStatus() : VersionStatus.DRAFT)
                .changelog(request.getChangelog())
                .build();

        ApiVersion saved = apiVersionRepository.save(apiVersion);
        return mapToResponse(saved);
    }

    public ApiVersionResponse updateVersion(Long apiId, Long versionId, ApiVersionRequest request) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(apiId)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + apiId));

        ApiVersion apiVersion = apiVersionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("API Version not found with id: " + versionId));

        if (!apiVersion.getApiDefinition().getId().equals(apiId)) {
            throw new EntityNotFoundException("API Version not found with id: " + versionId);
        }

        apiVersion.setVersionNumber(request.getVersionNumber());
        apiVersion.setSpec(request.getSpec());
        apiVersion.setSpecFormat(request.getSpecFormat());
        if (request.getStatus() != null) {
            apiVersion.setStatus(request.getStatus());
        }
        apiVersion.setChangelog(request.getChangelog());

        ApiVersion updated = apiVersionRepository.save(apiVersion);
        return mapToResponse(updated);
    }

    private ApiVersionResponse mapToResponse(ApiVersion apiVersion) {
        return ApiVersionResponse.builder()
                .id(apiVersion.getId())
                .apiDefinitionId(apiVersion.getApiDefinition().getId())
                .versionNumber(apiVersion.getVersionNumber())
                .spec(apiVersion.getSpec())
                .specFormat(apiVersion.getSpecFormat())
                .status(apiVersion.getStatus())
                .changelog(apiVersion.getChangelog())
                .createdAt(apiVersion.getCreatedAt())
                .updatedAt(apiVersion.getUpdatedAt())
                .build();
    }
}
