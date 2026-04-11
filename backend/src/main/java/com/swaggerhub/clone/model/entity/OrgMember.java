package com.swaggerhub.clone.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "org_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgMember {

    @EmbeddedId
    private OrgMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orgId")
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "role", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberRole role = MemberRole.MEMBER;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    public enum MemberRole {
        OWNER, ADMIN, MEMBER
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrgMemberId implements Serializable {

        @Column(name = "org_id")
        private UUID orgId;

        @Column(name = "user_id")
        private UUID userId;
    }
}
