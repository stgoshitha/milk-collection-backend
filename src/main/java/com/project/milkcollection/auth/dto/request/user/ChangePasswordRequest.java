package com.project.milkcollection.auth.dto.request.user;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank(message = "Current Password is required")
        String currentPassword,

        @NotBlank(message = "New Password is required")
        @Size(
                min = 8,
                max = 100,
                message = "New password must be between 8 and 100 characters"
        )
        @Pattern(
                regexp = RegexConstants.PASSWORD,
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String newPassword,

        @NotBlank(message = "Confirm Password is required")
        String confirmPassword
) {
}
