package com.swaggerhub.clone.model.dto.request;

import com.swaggerhub.clone.model.entity.ApiSpec;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateSpecRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;

    @NotBlank(message = "Version is required")
    @Size(max = 100, message = "Version must not exceed 100 characters")
    private String version;

    private UUID orgId;

    private ApiSpec.Visibility visibility = ApiSpec.Visibility.PRIVATE;

    private List<String> tags;

    private String contentYaml;
}
