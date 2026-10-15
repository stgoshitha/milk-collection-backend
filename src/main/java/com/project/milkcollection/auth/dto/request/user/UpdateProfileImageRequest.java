package com.project.milkcollection.auth.dto.request.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileImageRequest(
        @NotNull(message = "Profile image is required")
        MultipartFile profile
) {
}
