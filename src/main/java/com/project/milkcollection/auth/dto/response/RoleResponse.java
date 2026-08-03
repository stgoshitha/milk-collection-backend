package com.project.milkcollection.auth.dto.response;

import com.project.milkcollection.common.enums.CommonStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponse(
        UUID roleId,
        String roleName,
        String description,
        CommonStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
