package com.project.milkcollection.auth.dto.request.user;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record UpdateUserRequest(
        @NotBlank(message = "Full name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Full name must be between 2 and 100 characters"
        )
        @Pattern(
                regexp = RegexConstants.NAME,
                message = "Full name can contain only letters and spaces"
        )
        String fullName,

        @NotBlank(message = "Username is required")
        @Size(
                min = 4,
                max = 30,
                message = "Username must be between 4 and 30 characters"
        )
        @Pattern(
                regexp = RegexConstants.USERNAME,
                message = "Username format is invalid"
        )
        String username,

        @NotBlank(message = "Email is required")
        @Size(
                max = 150,
                message = "Email cannot exceed 150 characters"
        )
        @Email(message = "Invalid email address")
        @Pattern(
                regexp = RegexConstants.EMAIL,
                message = "Email format is invalid"
        )
        String email,

        @NotNull(message = "Role ID is required")
        UUID roleId
) {
}
