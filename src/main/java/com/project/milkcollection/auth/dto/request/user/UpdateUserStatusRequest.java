package com.project.milkcollection.auth.dto.request.user;

import com.project.milkcollection.auth.entity.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(

        @NotNull(message = "User status is required")
        UserStatus status

) {
}
