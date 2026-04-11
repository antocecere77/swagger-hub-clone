package com.swaggerhub.clone.model.dto.response;

import com.swaggerhub.clone.model.entity.OrgMember;
import com.swaggerhub.clone.model.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgResponse {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String website;
    private UserResponse owner;
    private int membersCount;
    private List<MemberInfo> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrgResponse from(Organization org) {
        return OrgResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .slug(org.getSlug())
                .description(org.getDescription())
                .website(org.getWebsite())
                .owner(UserResponse.from(org.getOwner()))
                .membersCount(org.getMembers().size())
                .members(org.getMembers().stream().map(MemberInfo::from).collect(Collectors.toList()))
                .createdAt(org.getCreatedAt())
                .updatedAt(org.getUpdatedAt())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private UserResponse user;
        private OrgMember.MemberRole role;
        private LocalDateTime joinedAt;

        public static MemberInfo from(OrgMember member) {
            return MemberInfo.builder()
                    .user(UserResponse.from(member.getUser()))
                    .role(member.getRole())
                    .joinedAt(member.getJoinedAt())
                    .build();
        }
    }
}
