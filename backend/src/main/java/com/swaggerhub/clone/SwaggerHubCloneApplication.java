package com.swaggerhub.clone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SwaggerHubCloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerHubCloneApplication.class, args);
        log.info("SwaggerHub Clone started — http://localhost:8080");
        log.info("Swagger UI  → http://localhost:8080/swagger-ui.html");
        log.info("H2 Console  → http://localhost:8080/h2-console");
    }
}
