package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.ApiDefinitionRequest;
import com.swaggerhub.clone.dto.ApiDefinitionResponse;
import com.swaggerhub.clone.dto.PageResponse;
import com.swaggerhub.clone.model.ApiDefinition;
import com.swaggerhub.clone.model.ApiStatus;
import com.swaggerhub.clone.model.ApiVisibility;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiDefinitionService {

    private final ApiDefinitionRepository apiDefinitionRepository;
    private final ApiVersionRepository apiVersionRepository;

    public PageResponse<ApiDefinitionResponse> getApis(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        
        Page<ApiDefinition> result;
        if (search != null && !search.isEmpty()) {
            result = apiDefinitionRepository.findByStatusNotAndNameContainingIgnoreCase(
                    ApiStatus.DELETED, search, pageable);
        } else {
            result = apiDefinitionRepository.findByStatusNot(ApiStatus.DELETED, pageable);
        }

        return PageResponse.<ApiDefinitionResponse>builder()
                .content(result.getContent().stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList()))
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    public ApiDefinitionResponse getApiById(Long id) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + id));
        
        if (apiDefinition.getStatus() == ApiStatus.DELETED) {
            throw new EntityNotFoundException("API Definition not found with id: " + id);
        }
        
        return mapToResponse(apiDefinition);
    }

    public ApiDefinitionResponse createApi(ApiDefinitionRequest request) {
        ApiDefinition apiDefinition = ApiDefinition.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .visibility(request.getVisibility() != null ? request.getVisibility() : ApiVisibility.PUBLIC)
                .status(ApiStatus.ACTIVE)
                .ownerId("default-user")
                .tags(request.getTags())
                .build();

        ApiDefinition saved = apiDefinitionRepository.save(apiDefinition);
        return mapToResponse(saved);
    }

    public ApiDefinitionResponse updateApi(Long id, ApiDefinitionRequest request) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + id));

        if (apiDefinition.getStatus() == ApiStatus.DELETED) {
            throw new EntityNotFoundException("API Definition not found with id: " + id);
        }

        apiDefinition.setName(request.getName());
        apiDefinition.setDescription(request.getDescription());
        apiDefinition.setCategory(request.getCategory());
        if (request.getVisibility() != null) {
            apiDefinition.setVisibility(request.getVisibility());
        }
        apiDefinition.setTags(request.getTags());

        ApiDefinition updated = apiDefinitionRepository.save(apiDefinition);
        return mapToResponse(updated);
    }

    public void deleteApi(Long id) {
        ApiDefinition apiDefinition = apiDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("API Definition not found with id: " + id));

        apiDefinition.setStatus(ApiStatus.DELETED);
        apiDefinitionRepository.save(apiDefinition);
    }

    private ApiDefinitionResponse mapToResponse(ApiDefinition apiDefinition) {
        long versionsCount = apiVersionRepository.countByApiDefinitionId(apiDefinition.getId());
        
        return ApiDefinitionResponse.builder()
                .id(apiDefinition.getId())
                .name(apiDefinition.getName())
                .description(apiDefinition.getDescription())
                .category(apiDefinition.getCategory())
                .visibility(apiDefinition.getVisibility())
                .status(apiDefinition.getStatus())
                .ownerId(apiDefinition.getOwnerId())
                .tags(apiDefinition.getTags())
                .versionsCount(versionsCount)
                .createdAt(apiDefinition.getCreatedAt())
                .updatedAt(apiDefinition.getUpdatedAt())
                .build();
    }
}
