package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleStatusRequest;
import com.project.milkcollection.auth.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest createRoleRequest);

    RoleResponse getRoleById(UUID roleId);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRole(UUID roleId, UpdateRoleRequest request);

    RoleResponse updateRoleStatus(UUID roleId, UpdateRoleStatusRequest updateRoleStatusRequest);

    RoleResponse deleteRole(UUID roleId);
}
