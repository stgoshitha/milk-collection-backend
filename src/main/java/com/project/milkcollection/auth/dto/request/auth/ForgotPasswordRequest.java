package com.project.milkcollection.auth.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

        @NotBlank(message = "Username or email is required")
        String loginIdentifier
) {
}
