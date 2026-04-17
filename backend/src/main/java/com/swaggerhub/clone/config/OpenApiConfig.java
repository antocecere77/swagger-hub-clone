package com.swaggerhub.clone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SwaggerHub Clone API")
                .version("1.0.0")
                .description("REST API for SwaggerHub Clone — manage OpenAPI definitions and versions")
                .contact(new Contact().name("Antonio Cecere").email("antocecere77@gmail.com"))
                .license(new License().name("MIT")));
    }
}
