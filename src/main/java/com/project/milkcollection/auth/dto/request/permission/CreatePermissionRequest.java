package com.project.milkcollection.auth.dto.request.permission;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreatePermissionRequest(

        @NotNull(message = "Module ID is required")
        UUID systemModuleId,

        @NotBlank(message = "Permission name is required")
        @Size(
                max = 100,
                message = "Permission name cannot exceed 100 characters"
        )
        @Pattern(
                regexp = RegexConstants.PERMISSION_NAME,
                message = "Permission name must contain only uppercase letters and underscores"
        )
        String permissionName,

        @Size(
                max = 255,
                message = "Description cannot exceed 255 characters"
        )
        String description

) {
}