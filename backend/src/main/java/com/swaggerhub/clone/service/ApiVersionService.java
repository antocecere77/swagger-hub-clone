package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.ApiVersionRequest;
import com.swaggerhub.clone.dto.ApiVersionResponse;
import com.swaggerhub.clone.exception.DuplicateVersionException;
import com.swaggerhub.clone.exception.ResourceNotFoundException;
import com.swaggerhub.clone.model.ApiDefinition;
import com.swaggerhub.clone.model.ApiVersion;
import com.swaggerhub.clone.model.VersionStatus;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
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
        ensureApiExists(apiId);
        return apiVersionRepository.findByApiDefinitionIdOrderByCreatedAtDesc(apiId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApiVersionResponse getVersionById(Long apiId, Long versionId) {
        ensureApiExists(apiId);
        ApiVersion apiVersion = apiVersionRepository.findByApiDefinitionIdAndId(apiId, versionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Version " + versionId + " not found for API " + apiId));
        return mapToResponse(apiVersion);
    }

    public ApiVersionResponse createVersion(Long apiId, ApiVersionRequest request) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(apiId)
                .orElseThrow(() -> new ResourceNotFoundException("API Definition not found with id: " + apiId));

        if (apiVersionRepository.existsByApiDefinitionIdAndVersionNumber(apiId, request.getVersionNumber())) {
            throw new DuplicateVersionException(
                    "Version " + request.getVersionNumber() + " already exists for this API");
        }

        ApiVersion apiVersion = ApiVersion.builder()
                .apiDefinition(apiDefinition)
                .versionNumber(request.getVersionNumber())
                .spec(request.getSpec())
                .specFormat(request.getSpecFormat() != null ? request.getSpecFormat() : "YAML")
                .status(request.getStatus() != null ? request.getStatus() : VersionStatus.DRAFT)
                .changelog(request.getChangelog())
                .build();

        ApiVersion saved = apiVersionRepository.save(apiVersion);
        return mapToResponse(saved);
    }

    public ApiVersionResponse updateVersion(Long apiId, Long versionId, ApiVersionRequest request) {
        ensureApiExists(apiId);
        ApiVersion apiVersion = apiVersionRepository.findByApiDefinitionIdAndId(apiId, versionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Version " + versionId + " not found for API " + apiId));

        if (!apiVersion.getVersionNumber().equals(request.getVersionNumber()) &&
                apiVersionRepository.existsByApiDefinitionIdAndVersionNumber(apiId, request.getVersionNumber())) {
            throw new DuplicateVersionException(
                    "Version " + request.getVersionNumber() + " already exists for this API");
        }

        apiVersion.setVersionNumber(request.getVersionNumber());
        if (request.getSpec() != null) apiVersion.setSpec(request.getSpec());
        if (request.getSpecFormat() != null) apiVersion.setSpecFormat(request.getSpecFormat());
        if (request.getStatus() != null) apiVersion.setStatus(request.getStatus());
        if (request.getChangelog() != null) apiVersion.setChangelog(request.getChangelog());

        ApiVersion updated = apiVersionRepository.save(apiVersion);
        return mapToResponse(updated);
    }

    private void ensureApiExists(Long apiId) {
        if (!apiDefinitionRepository.existsById(apiId)) {
            throw new ResourceNotFoundException("API Definition not found with id: " + apiId);
        }
    }

    private ApiVersionResponse mapToResponse(ApiVersion apiVersion) {
        return ApiVersionResponse.builder()
                .id(apiVersion.getId())
                .apiDefinitionId(apiVersion.getApiDefinition().getId())
                .apiName(apiVersion.getApiDefinition().getName())
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
