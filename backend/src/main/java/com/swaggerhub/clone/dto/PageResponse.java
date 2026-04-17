package com.swaggerhub.clone.dto;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int currentPage,
    int pageSize,
    boolean first,
    boolean last
) {}
