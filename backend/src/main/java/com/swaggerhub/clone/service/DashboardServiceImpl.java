package com.swaggerhub.clone.service;

import com.swaggerhub.clone.model.ApiStatus;
import com.swaggerhub.clone.model.VersionStatus;
import com.swaggerhub.clone.repository.ApiDefinitionRepository;
import com.swaggerhub.clone.repository.ApiVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ApiDefinitionRepository apiRepository;
    private final ApiVersionRepository versionRepository;

    public record DashboardStats(
        long totalApis,
        long publishedVersions,
        long draftVersions,
        long totalVersions
    ) {}

    @Override
    public DashboardStats getStats() {
        log.debug("Calculating dashboard stats");
        long totalApis         = apiRepository.countByStatus(ApiStatus.ACTIVE);
        long totalVersions     = versionRepository.count();
        long publishedVersions = versionRepository.findAll().stream()
            .filter(v -> v.getStatus() == VersionStatus.PUBLISHED).count();
        long draftVersions     = versionRepository.findAll().stream()
            .filter(v -> v.getStatus() == VersionStatus.DRAFT).count();
        log.debug("Stats: totalApis={}, published={}, draft={}, totalVersions={}",
            totalApis, publishedVersions, draftVersions, totalVersions);
        return new DashboardStats(totalApis, publishedVersions, draftVersions, totalVersions);
    }
}
