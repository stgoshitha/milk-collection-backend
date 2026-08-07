package com.project.milkcollection.auth.dto.request;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(

        @NotBlank(message = "Role code is required")
        @Pattern(
                regexp = RegexConstants.ROLE_CODE,
                message = "Invalid role code format"
        )
        String roleCode,

        @NotBlank(message = "Role name is required")
        @Size(max = 50, message = "Role name cannot exceed 50 characters")
        @Pattern(
                regexp = RegexConstants.ROLE_NAME,
                message = "Invalid role name format"
        )
        String roleName,

        @Size(max = 225, message = "Description cannot exceed 225 characters")
        String description

) {
}