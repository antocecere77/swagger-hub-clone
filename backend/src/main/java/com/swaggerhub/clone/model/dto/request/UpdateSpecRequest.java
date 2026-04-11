package com.swaggerhub.clone.model.dto.request;

import com.swaggerhub.clone.model.entity.ApiSpec;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateSpecRequest {

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;

    @Size(max = 100, message = "Version must not exceed 100 characters")
    private String version;

    private ApiSpec.Visibility visibility;

    private List<String> tags;

    private String contentYaml;
}
