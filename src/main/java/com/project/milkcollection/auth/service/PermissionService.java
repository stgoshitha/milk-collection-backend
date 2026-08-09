package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.permission.CreatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionStatusRequest;
import com.project.milkcollection.auth.dto.response.PermissionResponse;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    PermissionResponse createPermission(CreatePermissionRequest createPermissionRequest);

    PermissionResponse getPermissionById(UUID permissionId);

    List<PermissionResponse> getAllPermissions();

    PermissionResponse updatePermission(
            UUID permissionId,
            UpdatePermissionRequest updatePermissionRequest
    );

    PermissionResponse updatePermissionStatus(
            UUID permissionId,
            UpdatePermissionStatusRequest updatePermissionStatusRequest
    );
}