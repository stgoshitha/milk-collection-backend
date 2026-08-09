package com.project.milkcollection.auth.dto.request.systemmodule;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateSystemModuleOrderItem(

        @NotNull(message = "Module ID is required")
        UUID systemModuleId,

        @NotNull(message = "Display order is required")
        @Min(
                value = 1,
                message = "Display order must be greater than 0"
        )
        Integer displayOrder

) {
}
