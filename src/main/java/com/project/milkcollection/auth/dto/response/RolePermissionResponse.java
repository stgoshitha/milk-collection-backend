package com.project.milkcollection.auth.dto.response;

import java.util.UUID;

public record RolePermissionResponse(
        UUID rolePermissionId,
        UUID roleId,
        String roleName,
        UUID permissionId,
        String permissionName
) {
}
