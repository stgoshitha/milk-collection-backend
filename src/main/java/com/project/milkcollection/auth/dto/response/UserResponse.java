package com.project.milkcollection.auth.dto.response;

import com.project.milkcollection.auth.entity.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String fullName,
        String username,
        String email,
        UUID roleId,
        String roleName,
        UserStatus status,
        String profileImgUrl,
        LocalDateTime lastLogin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
