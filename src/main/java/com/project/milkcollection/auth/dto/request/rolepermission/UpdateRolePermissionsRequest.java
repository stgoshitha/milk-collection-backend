package com.project.milkcollection.auth.dto.request.rolepermission;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record UpdateRolePermissionsRequest(

        @NotEmpty(message = "At least one permission is required")
        List<UUID> permissionIds
) {
}
