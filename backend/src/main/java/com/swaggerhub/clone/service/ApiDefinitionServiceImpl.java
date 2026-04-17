package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.*;
import com.swaggerhub.clone.exception.ResourceNotFoundException;
import com.swaggerhub.clone.model.*;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiDefinitionServiceImpl implements IApiDefinitionService {

    private final ApiDefinitionRepository apiRepository;
    private final ApiVersionRepository versionRepository;

    @Override
    public PageResponse<ApiDefinitionResponse> getApis(int page, int size, String search, String category, String sort) {
        log.debug("Fetching APIs - page={}, size={}, search='{}', category='{}', sort='{}'", page, size, search, category, sort);

        Sort sorting = Sort.by(Sort.Direction.DESC, "updatedAt");
        if ("name".equals(sort))      sorting = Sort.by(Sort.Direction.ASC, "name");
        if ("createdAt".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<ApiDefinition> result;

        if ((search == null || search.isBlank()) && (category == null || category.isBlank())) {
            result = apiRepository.findByStatus(ApiStatus.ACTIVE, pageable);
        } else {
            result = apiRepository.findByStatusAndSearchAndCategory(ApiStatus.ACTIVE, search, category, pageable);
        }

        log.debug("Found {} APIs (total={})", result.getNumberOfElements(), result.getTotalElements());

        return new PageResponse<>(
            result.getContent().stream().map(this::toResponse).toList(),
            result.getTotalElements(),
            result.getTotalPages(),
            result.getNumber(),
            result.getSize(),
            result.isFirst(),
            result.isLast()
        );
    }

    @Override
    public ApiDefinitionResponse getApiById(Long id) {
        log.debug("Fetching API with id={}", id);
        ApiDefinition api = findActiveApi(id);
        log.debug("Found API: '{}'", api.getName());
        return toResponse(api);
    }

    @Override
    @Transactional
    public ApiDefinitionResponse createApi(ApiDefinitionRequest request) {
        log.info("Creating new API: '{}'", request.name());
        ApiDefinition api = ApiDefinition.builder()
            .name(request.name())
            .description(request.description())
            .category(request.category())
            .visibility(request.visibility() != null ? request.visibility() : ApiVisibility.PUBLIC)
            .tags(request.tags())
            .ownerId("default-user")
            .status(ApiStatus.ACTIVE)
            .build();
        ApiDefinition saved = apiRepository.save(api);
        log.info("API created with id={}, name='{}'", saved.getId(), saved.getName());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ApiDefinitionResponse updateApi(Long id, ApiDefinitionRequest request) {
        log.info("Updating API with id={}", id);
        ApiDefinition api = findActiveApi(id);
        api.setName(request.name());
        api.setDescription(request.description());
        api.setCategory(request.category());
        if (request.visibility() != null) api.setVisibility(request.visibility());
        api.setTags(request.tags());
        ApiDefinition saved = apiRepository.save(api);
        log.info("API id={} updated successfully", id);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteApi(Long id) {
        log.info("Soft-deleting API with id={}", id);
        ApiDefinition api = findActiveApi(id);
        api.setStatus(ApiStatus.DELETED);
        apiRepository.save(api);
        log.info("API id={} soft-deleted", id);
    }

    @Override
    public long countActiveApis() {
        long count = apiRepository.countByStatus(ApiStatus.ACTIVE);
        log.debug("Active APIs count={}", count);
        return count;
    }

    private ApiDefinition findActiveApi(Long id) {
        return apiRepository.findById(id)
            .filter(a -> a.getStatus() == ApiStatus.ACTIVE)
            .orElseThrow(() -> {
                log.warn("API not found or deleted: id={}", id);
                return new ResourceNotFoundException("API not found with id: " + id);
            });
    }

    private ApiDefinitionResponse toResponse(ApiDefinition api) {
        long versionsCount = versionRepository.countByApiDefinitionId(api.getId());
        return new ApiDefinitionResponse(
            api.getId(), api.getName(), api.getDescription(), api.getCategory(),
            api.getVisibility(), api.getStatus(), api.getOwnerId(), api.getTags(),
            versionsCount, api.getCreatedAt(), api.getUpdatedAt()
        );
    }
}
