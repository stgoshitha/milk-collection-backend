package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.rolepermission.UpdateRolePermissionsRequest;
import com.project.milkcollection.auth.dto.response.RolePermissionResponse;

import java.util.List;
import java.util.UUID;

public interface RolePermissionService {

    List<RolePermissionResponse> getRolePermissions(UUID roleId);

    List<RolePermissionResponse> updateRolePermissions(
            UUID roleId,
            UpdateRolePermissionsRequest updateRolePermissionsRequest
    );

}