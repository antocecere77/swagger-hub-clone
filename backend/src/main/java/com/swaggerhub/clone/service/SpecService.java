package com.swaggerhub.clone.service;

import com.swaggerhub.clone.exception.InvalidSpecException;
import com.swaggerhub.clone.exception.ResourceNotFoundException;
import com.swaggerhub.clone.exception.UnauthorizedException;
import com.swaggerhub.clone.model.dto.request.CreateSpecRequest;
import com.swaggerhub.clone.model.dto.request.CreateVersionRequest;
import com.swaggerhub.clone.model.dto.request.UpdateSpecRequest;
import com.swaggerhub.clone.model.dto.response.PagedResponse;
import com.swaggerhub.clone.model.dto.response.SpecResponse;
import com.swaggerhub.clone.model.dto.response.SpecVersionResponse;
import com.swaggerhub.clone.model.entity.ApiSpec;
import com.swaggerhub.clone.model.entity.ApiTag;
import com.swaggerhub.clone.model.entity.Organization;
import com.swaggerhub.clone.model.entity.SpecVersion;
import com.swaggerhub.clone.model.entity.User;
import com.swaggerhub.clone.repository.ApiSpecRepository;
import com.swaggerhub.clone.repository.ApiTagRepository;
import com.swaggerhub.clone.repository.OrganizationRepository;
import com.swaggerhub.clone.repository.SpecVersionRepository;
import com.swaggerhub.clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SpecService {

    private final ApiSpecRepository specRepository;
    private final ApiTagRepository tagRepository;
    private final SpecVersionRepository versionRepository;
    private final OrganizationRepository orgRepository;
    private final UserRepository userRepository;

    public SpecResponse createSpec(CreateSpecRequest request, String userEmail) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Organization org = null;
        if (request.getOrgId() != null) {
            org = orgRepository.findById(request.getOrgId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        }

        if (request.getContentYaml() != null && !request.getContentYaml().isBlank()) {
            validateYaml(request.getContentYaml());
        }

        ApiSpec spec = ApiSpec.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .version(request.getVersion())
                .organization(org)
                .owner(owner)
                .visibility(request.getVisibility() != null ? request.getVisibility() : ApiSpec.Visibility.PRIVATE)
                .tags(new ArrayList<>())
                .versions(new ArrayList<>())
                .build();

        spec = specRepository.save(spec);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            saveTags(spec, request.getTags());
        }

        if (request.getContentYaml() != null && !request.getContentYaml().isBlank()) {
            SpecVersion version = SpecVersion.builder()
                    .spec(spec)
                    .versionNumber(request.getVersion())
                    .contentYaml(request.getContentYaml())
                    .createdBy(owner)
                    .build();
            versionRepository.save(version);
        }

        spec = specRepository.findById(spec.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Spec not found after save"));

        log.info("Created new API spec: {} by {}", spec.getTitle(), userEmail);
        return SpecResponse.from(spec);
    }

    @Transactional(readOnly = true)
    public SpecResponse getSpec(UUID id, String userEmail) {
        ApiSpec spec = specRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + id));

        checkReadAccess(spec, userEmail);
        return SpecResponse.from(spec);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SpecResponse> getMySpecs(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<ApiSpec> specs = specRepository.findByOwnerId(user.getId(), pageable);
        return PagedResponse.from(specs.map(SpecResponse::from));
    }

    @Transactional(readOnly = true)
    public PagedResponse<SpecResponse> getPublicSpecs(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<ApiSpec> specs;

        if (query != null && !query.isBlank()) {
            specs = specRepository.searchPublic(query, pageable);
        } else {
            specs = specRepository.findByVisibility(ApiSpec.Visibility.PUBLIC, pageable);
        }

        return PagedResponse.from(specs.map(SpecResponse::from));
    }

    public SpecResponse updateSpec(UUID id, UpdateSpecRequest request, String userEmail) {
        ApiSpec spec = specRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + id));

        checkWriteAccess(spec, userEmail);

        if (request.getContentYaml() != null && !request.getContentYaml().isBlank()) {
            validateYaml(request.getContentYaml());
        }

        if (request.getTitle() != null) spec.setTitle(request.getTitle());
        if (request.getDescription() != null) spec.setDescription(request.getDescription());
        if (request.getVersion() != null) spec.setVersion(request.getVersion());
        if (request.getVisibility() != null) spec.setVisibility(request.getVisibility());

        if (request.getTags() != null) {
            tagRepository.deleteBySpecId(spec.getId());
            spec.getTags().clear();
            saveTags(spec, request.getTags());
        }

        spec = specRepository.save(spec);
        log.info("Updated API spec: {}", id);
        return SpecResponse.from(spec);
    }

    public void deleteSpec(UUID id, String userEmail) {
        ApiSpec spec = specRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + id));

        checkWriteAccess(spec, userEmail);
        specRepository.delete(spec);
        log.info("Deleted API spec: {}", id);
    }

    public SpecVersionResponse createVersion(UUID specId, CreateVersionRequest request, String userEmail) {
        ApiSpec spec = specRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + specId));

        checkWriteAccess(spec, userEmail);
        validateYaml(request.getContentYaml());

        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SpecVersion version = SpecVersion.builder()
                .spec(spec)
                .versionNumber(request.getVersionNumber())
                .contentYaml(request.getContentYaml())
                .notes(request.getNotes())
                .createdBy(creator)
                .build();

        version = versionRepository.save(version);
        log.info("Created version {} for spec {}", request.getVersionNumber(), specId);
        return SpecVersionResponse.from(version);
    }

    @Transactional(readOnly = true)
    public List<SpecVersionResponse> getVersions(UUID specId, String userEmail) {
        ApiSpec spec = specRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + specId));

        checkReadAccess(spec, userEmail);

        return versionRepository.findBySpecIdOrderByCreatedAtDesc(specId)
                .stream()
                .map(SpecVersionResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SpecVersionResponse getVersion(UUID specId, UUID versionId, String userEmail) {
        ApiSpec spec = specRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + specId));

        checkReadAccess(spec, userEmail);

        SpecVersion version = versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + versionId));

        return SpecVersionResponse.from(version);
    }

    @Transactional(readOnly = true)
    public SpecVersionResponse getLatestVersion(UUID specId, String userEmail) {
        ApiSpec spec = specRepository.findById(specId)
                .orElseThrow(() -> new ResourceNotFoundException("API spec not found with id: " + specId));

        checkReadAccess(spec, userEmail);

        SpecVersion version = versionRepository.findTopBySpecIdOrderByCreatedAtDesc(specId)
                .orElseThrow(() -> new ResourceNotFoundException("No versions found for spec: " + specId));

        return SpecVersionResponse.from(version);
    }

    private void validateYaml(String yaml) {
        try {
            Yaml yamlParser = new Yaml();
            Object parsed = yamlParser.load(yaml);
            if (parsed == null) {
                throw new InvalidSpecException("YAML content is empty or invalid");
            }
        } catch (Exception e) {
            if (e instanceof InvalidSpecException) throw e;
            throw new InvalidSpecException("Invalid YAML: " + e.getMessage());
        }
    }

    private void saveTags(ApiSpec spec, List<String> tagNames) {
        List<ApiTag> tags = tagNames.stream()
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .map(tagName -> ApiTag.builder().spec(spec).tag(tagName.trim().toLowerCase()).build())
                .collect(Collectors.toList());
        tagRepository.saveAll(tags);
    }

    private void checkReadAccess(ApiSpec spec, String userEmail) {
        if (spec.getVisibility() == ApiSpec.Visibility.PUBLIC) return;
        if (userEmail == null) throw new UnauthorizedException("Authentication required");
        if (spec.getOwner().getEmail().equals(userEmail)) return;
        throw new UnauthorizedException("You do not have access to this spec");
    }

    private void checkWriteAccess(ApiSpec spec, String userEmail) {
        if (userEmail == null) throw new UnauthorizedException("Authentication required");
        if (!spec.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have permission to modify this spec");
        }
    }
}
