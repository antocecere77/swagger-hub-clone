package com.swaggerhub.clone.service;

import com.swaggerhub.clone.dto.*;

public interface ApiDefinitionService {
    PageResponse<ApiDefinitionResponse> getApis(int page, int size, String search, String category, String sort);
    ApiDefinitionResponse getApiById(Long id);
    ApiDefinitionResponse createApi(ApiDefinitionRequest request);
    ApiDefinitionResponse updateApi(Long id, ApiDefinitionRequest request);
    void deleteApi(Long id);
    long countActiveApis();
}
