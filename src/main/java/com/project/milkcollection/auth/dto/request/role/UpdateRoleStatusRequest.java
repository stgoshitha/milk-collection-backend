package com.project.milkcollection.auth.dto.request.role;

import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleStatusRequest(

        @NotNull(message = "Status is required")
        CommonStatus status

) {
}
