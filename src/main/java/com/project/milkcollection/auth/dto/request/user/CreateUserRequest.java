package com.project.milkcollection.auth.dto.request.user;

import com.project.milkcollection.common.constants.RegexConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUserRequest(

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

        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                max = 100,
                message = "Password must be between 8 and 100 characters"
        )
        @Pattern(
                regexp = RegexConstants.PASSWORD,
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password,

        @NotNull(message = "Role ID is required")
        UUID roleId

) {
}