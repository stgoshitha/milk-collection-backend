package com.project.milkcollection.auth.dto.request.permission;

import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePermissionStatusRequest(

        @NotNull(message = "Status is required")
        CommonStatus status

) {
}