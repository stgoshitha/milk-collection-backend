package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleStatusRequest;
import com.project.milkcollection.auth.dto.response.RoleResponse;
import com.project.milkcollection.auth.service.RoleService;
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
@RequestMapping(SecurityConstants.ROLE_BASE_URL)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ) {

        RoleResponse response = roleService.createRole(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Role " +  ResponseMessage.CREATED_SUCCESSFULLY,
                                response
                        )
                );
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(
            @PathVariable UUID roleId) {

        RoleResponse role = roleService.getRoleById(roleId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Role " + ResponseMessage.FETCH_SUCCESSFULLY,
                        role
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles(){

        List<RoleResponse> roles = roleService.getAllRoles();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Roles " + ResponseMessage.FETCH_SUCCESSFULLY,
                        roles
                )
        );
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRoleRequest updateRoleRequest
    ){

        RoleResponse role = roleService.updateRole(roleId, updateRoleRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Role " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        role
                )
        );

    }

    @PatchMapping("/{roleId}/status")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRoleStatus(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRoleStatusRequest updateRoleStatusRequest
    ) {

        RoleResponse role = roleService.updateRoleStatus(roleId, updateRoleStatusRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Role status " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        role
                )
        );
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> deleteRole(
            @PathVariable UUID roleId
    ){

        RoleResponse role = roleService.deleteRole(roleId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ResponseMessage.ROLE_DEACTIVATED_SUCCESSFULLY,
                        role
                )
        );
    }
}
