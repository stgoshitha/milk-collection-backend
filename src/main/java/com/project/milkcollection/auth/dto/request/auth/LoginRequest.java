package com.project.milkcollection.auth.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username or email is required")
        String loginIdentifier,

        @NotBlank(message = "Password is required")
        String password

) {
}