package com.project.milkcollection.auth.dto.request;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.*;

public record CreateSystemModuleRequest(

        @NotBlank(message = "Module name is required")
        @Size(max = 100, message = "Module name cannot exceed 100 characters")
        @Pattern(
                regexp = RegexConstants.SYSTEM_MODULE_NAME,
                message = "Module name must start with a letter and contain only letters, numbers, and spaces"
        )
        String systemModuleName,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @Size(max = 100, message = "Icon cannot exceed 100 characters")
        @Pattern(
                regexp = RegexConstants.SYSTEM_MODULE_ICON,
                message = "Icon must contain only letters and numbers"
        )
        String icon

) {
}
