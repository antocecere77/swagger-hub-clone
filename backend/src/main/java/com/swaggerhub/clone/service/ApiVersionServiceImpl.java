package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.*;
import com.swaggerhub.clone.exception.*;
import com.swaggerhub.clone.model.*;
import com.swaggerhub.clone.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiVersionServiceImpl implements IApiVersionService {

    private final ApiVersionRepository versionRepository;
    private final ApiDefinitionRepository apiRepository;

    @Override
    public List<ApiVersionResponse> getVersionsByApiId(Long apiId) {
        log.debug("Fetching versions for apiId={}", apiId);
        ensureApiExists(apiId);
        List<ApiVersionResponse> versions = versionRepository
            .findByApiDefinitionIdOrderByCreatedAtDesc(apiId)
            .stream().map(this::toResponse).toList();
        log.debug("Found {} versions for apiId={}", versions.size(), apiId);
        return versions;
    }

    @Override
    public ApiVersionResponse getVersionById(Long apiId, Long versionId) {
        log.debug("Fetching version id={} for apiId={}", versionId, apiId);
        ensureApiExists(apiId);
        ApiVersion version = versionRepository.findByApiDefinitionIdAndId(apiId, versionId)
            .orElseThrow(() -> {
                log.warn("Version not found: id={}, apiId={}", versionId, apiId);
                return new ResourceNotFoundException("Version " + versionId + " not found for API " + apiId);
            });
        return toResponse(version);
    }

    @Override
    @Transactional
    public ApiVersionResponse createVersion(Long apiId, ApiVersionRequest request) {
        log.info("Creating version '{}' for apiId={}", request.versionNumber(), apiId);
        ApiDefinition api = apiRepository.findById(apiId)
            .filter(a -> a.getStatus() == ApiStatus.ACTIVE)
            .orElseThrow(() -> {
                log.warn("API not found for version creation: id={}", apiId);
                return new ResourceNotFoundException("API not found with id: " + apiId);
            });

        if (versionRepository.existsByApiDefinitionIdAndVersionNumber(apiId, request.versionNumber())) {
            log.warn("Duplicate version '{}' for apiId={}", request.versionNumber(), apiId);
            throw new DuplicateVersionException("Version " + request.versionNumber() + " already exists for this API");
        }

        ApiVersion version = ApiVersion.builder()
            .apiDefinition(api)
            .versionNumber(request.versionNumber())
            .spec(request.spec())
            .specFormat(request.specFormat() != null ? request.specFormat() : "YAML")
            .status(request.status() != null ? request.status() : VersionStatus.DRAFT)
            .changelog(request.changelog())
            .build();

        ApiVersion saved = versionRepository.save(version);
        log.info("Version created: id={}, version='{}', apiId={}", saved.getId(), saved.getVersionNumber(), apiId);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ApiVersionResponse updateVersion(Long apiId, Long versionId, ApiVersionRequest request) {
        log.info("Updating version id={} for apiId={}", versionId, apiId);
        ensureApiExists(apiId);
        ApiVersion version = versionRepository.findByApiDefinitionIdAndId(apiId, versionId)
            .orElseThrow(() -> {
                log.warn("Version not found for update: id={}, apiId={}", versionId, apiId);
                return new ResourceNotFoundException("Version " + versionId + " not found for API " + apiId);
            });

        if (!version.getVersionNumber().equals(request.versionNumber()) &&
            versionRepository.existsByApiDefinitionIdAndVersionNumber(apiId, request.versionNumber())) {
            throw new DuplicateVersionException("Version " + request.versionNumber() + " already exists");
        }

        version.setVersionNumber(request.versionNumber());
        if (request.spec() != null)       version.setSpec(request.spec());
        if (request.specFormat() != null) version.setSpecFormat(request.specFormat());
        if (request.status() != null)     version.setStatus(request.status());
        if (request.changelog() != null)  version.setChangelog(request.changelog());

        ApiVersion saved = versionRepository.save(version);
        log.info("Version id={} updated successfully", versionId);
        return toResponse(saved);
    }

    private void ensureApiExists(Long apiId) {
        if (!apiRepository.existsById(apiId)) {
            log.warn("API not found: id={}", apiId);
            throw new ResourceNotFoundException("API not found with id: " + apiId);
        }
    }

    private ApiVersionResponse toResponse(ApiVersion v) {
        return new ApiVersionResponse(
            v.getId(), v.getApiDefinition().getId(), v.getApiDefinition().getName(),
            v.getVersionNumber(), v.getSpec(), v.getSpecFormat(),
            v.getStatus(), v.getChangelog(), v.getCreatedAt(), v.getUpdatedAt()
        );
    }
}
