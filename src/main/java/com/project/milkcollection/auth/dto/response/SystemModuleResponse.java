package com.project.milkcollection.auth.dto.response;

import com.project.milkcollection.common.enums.CommonStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SystemModuleResponse(
        UUID systemModuleId,
        String systemModuleName,
        String description,
        Integer displayOrder,
        String icon,
        CommonStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
