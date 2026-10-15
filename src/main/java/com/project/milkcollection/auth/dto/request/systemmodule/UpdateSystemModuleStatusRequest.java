package com.project.milkcollection.auth.dto.request.systemmodule;

import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.validation.constraints.NotNull;


public record UpdateSystemModuleStatusRequest(

        @NotNull(message = "Status is required")
        CommonStatus status

) {
}
