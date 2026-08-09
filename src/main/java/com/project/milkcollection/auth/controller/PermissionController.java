package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.permission.CreatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionStatusRequest;
import com.project.milkcollection.auth.dto.response.PermissionResponse;
import com.project.milkcollection.auth.service.PermissionService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(SecurityConstants.PERMISSION_BASE_URL)
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // Create a new permission
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @Valid @RequestBody CreatePermissionRequest createPermissionRequest) {

        PermissionResponse permission = permissionService.createPermission(
                createPermissionRequest
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Permission "
                                        + ResponseMessage.CREATED_SUCCESSFULLY,
                                permission
                        )
                );
    }

    // Get permission by ID
    @GetMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(
            @PathVariable UUID permissionId) {

        PermissionResponse permission = permissionService.getPermissionById(
                permissionId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permission "
                                + ResponseMessage.FETCH_SUCCESSFULLY,
                        permission
                )
        );
    }

    // Get all permissions
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {

        List<PermissionResponse> permissions =
                permissionService.getAllPermissions();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permissions "
                                + ResponseMessage.FETCH_SUCCESSFULLY,
                        permissions
                )
        );
    }

    // Update permission details
    @PutMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(
            @PathVariable UUID permissionId,
            @Valid @RequestBody UpdatePermissionRequest updatePermissionRequest) {

        PermissionResponse permission = permissionService.updatePermission(
                permissionId,
                updatePermissionRequest
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permission "
                                + ResponseMessage.UPDATED_SUCCESSFULLY,
                        permission
                )
        );
    }

    // Update permission status
    @PatchMapping("/{permissionId}/status")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermissionStatus(
            @PathVariable UUID permissionId,
            @Valid @RequestBody UpdatePermissionStatusRequest updatePermissionStatusRequest) {

        PermissionResponse permission =
                permissionService.updatePermissionStatus(
                        permissionId,
                        updatePermissionStatusRequest
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permission status "
                                + ResponseMessage.UPDATED_SUCCESSFULLY,
                        permission
                )
        );
    }
}