package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.rolepermission.UpdateRolePermissionsRequest;
import com.project.milkcollection.auth.dto.response.RolePermissionResponse;
import com.project.milkcollection.auth.service.RolePermissionService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    // Get all permissions assigned to a role
    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<List<RolePermissionResponse>>> getRolePermissions(
            @PathVariable UUID roleId) {

        List<RolePermissionResponse> permissions =
                rolePermissionService.getRolePermissions(roleId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Role permissions " +
                                ResponseMessage.FETCH_SUCCESSFULLY,
                        permissions
                )
        );
    }

    // Replace all permissions assigned to a role
    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<List<RolePermissionResponse>>> updateRolePermissions(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRolePermissionsRequest updateRolePermissionsRequest) {

        List<RolePermissionResponse> permissions =
                rolePermissionService.updateRolePermissions(
                        roleId,
                        updateRolePermissionsRequest
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Role permissions " +
                                ResponseMessage.UPDATED_SUCCESSFULLY,
                        permissions
                )
        );
    }
}