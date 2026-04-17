package com.swaggerhub.clone.service;

import com.swaggerhub.clone.model.ApiStatus;
import com.swaggerhub.clone.model.VersionStatus;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ApiDefinitionRepository apiRepository;
    private final ApiVersionRepository versionRepository;

    public record DashboardStats(
        long totalApis,
        long publishedVersions,
        long draftVersions,
        long totalVersions
    ) {}

    public DashboardStats getStats() {
        long totalApis = apiRepository.countByStatus(ApiStatus.ACTIVE);
        long totalVersions = versionRepository.count();
        long publishedVersions = versionRepository.findAll().stream()
            .filter(v -> v.getStatus() == VersionStatus.PUBLISHED).count();
        long draftVersions = versionRepository.findAll().stream()
            .filter(v -> v.getStatus() == VersionStatus.DRAFT).count();

        return new DashboardStats(totalApis, publishedVersions, draftVersions, totalVersions);
    }
}
