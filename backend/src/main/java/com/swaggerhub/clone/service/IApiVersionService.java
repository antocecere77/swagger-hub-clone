package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.*;
import java.util.List;

public interface IApiVersionService {
    List<ApiVersionResponse> getVersionsByApiId(Long apiId);
    ApiVersionResponse getVersionById(Long apiId, Long versionId);
    ApiVersionResponse createVersion(Long apiId, ApiVersionRequest request);
    ApiVersionResponse updateVersion(Long apiId, Long versionId, ApiVersionRequest request);
}
