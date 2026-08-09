package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.rolepermission.UpdateRolePermissionsRequest;
import com.project.milkcollection.auth.dto.response.RolePermissionResponse;
import com.project.milkcollection.auth.entity.Permission;
import com.project.milkcollection.auth.entity.Role;
import com.project.milkcollection.auth.entity.RolePermission;
import com.project.milkcollection.auth.mapper.RolePermissionMapper;
import com.project.milkcollection.auth.repository.PermissionRepository;
import com.project.milkcollection.auth.repository.RolePermissionRepository;
import com.project.milkcollection.auth.repository.RoleRepository;
import com.project.milkcollection.auth.service.RolePermissionService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    // Get all permissions assigned to a role
    @Override
    @Transactional(readOnly = true)
    public List<RolePermissionResponse> getRolePermissions(UUID roleId) {

        findRoleById(roleId);

        List<RolePermission> rolePermissions =
                rolePermissionRepository.findByRoleRoleId(roleId);

        return rolePermissionMapper.toResponseList(rolePermissions);
    }

    // Replace all permissions assigned to a role
    @Override
    @Transactional
    public List<RolePermissionResponse> updateRolePermissions(
            UUID roleId,
            UpdateRolePermissionsRequest updateRolePermissionsRequest) {

        Role role = findRoleById(roleId);

        List<Permission> permissions =
                permissionRepository.findAllById(updateRolePermissionsRequest.permissionIds());

        // Make sure every requested permission exists
        if (permissions.size() != updateRolePermissionsRequest.permissionIds().size()) {
            throw new ResourceNotFoundException(
                    ResponseMessage.PERMISSION_NOT_FOUND
            );
        }

        // Remove existing role-permission assignments
        rolePermissionRepository.deleteByRoleRoleId(roleId);

        // Create new role-permission assignments
        List<RolePermission> rolePermissions = permissions.stream()
                .map(permission -> RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build())
                .toList();

        List<RolePermission> savedRolePermissions =
                rolePermissionRepository.saveAll(rolePermissions);

        return rolePermissionMapper.toResponseList(savedRolePermissions);
    }

    // Find role by ID or throw exception
    private Role findRoleById(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ResponseMessage.ROLE_NOT_FOUND
                        )
                );
    }
}