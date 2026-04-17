package com.swaggerhub.clone.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_versions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_definition_id", nullable = false)
    private ApiDefinition apiDefinition;

    @Column(name = "version_number", nullable = false, length = 50)
    private String versionNumber;

    @Column(columnDefinition = "TEXT")
    private String spec;

    @Column(name = "spec_format", length = 10)
    @Builder.Default
    private String specFormat = "YAML";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VersionStatus status = VersionStatus.DRAFT;

    @Column(length = 1000)
    private String changelog;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
