package com.project.milkcollection.auth.dto.request.auth;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(

        @NotBlank(message = "New password is required")
        @Pattern(
                regexp = RegexConstants.PASSWORD,
                message = "Password format is invalid"
        )
        String newPassword,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}