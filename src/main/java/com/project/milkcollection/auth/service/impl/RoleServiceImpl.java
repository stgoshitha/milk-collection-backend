package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleRequest;
import com.project.milkcollection.auth.dto.request.UpdateRoleStatusRequest;
import com.project.milkcollection.auth.dto.response.RoleResponse;
import com.project.milkcollection.auth.entity.Role;
import com.project.milkcollection.auth.mapper.RoleMapper;
import com.project.milkcollection.auth.repository.RoleRepository;
import com.project.milkcollection.auth.service.RoleService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.enums.CommonStatus;
import com.project.milkcollection.exception.ConflictException;
import com.project.milkcollection.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    // Create a new role
    @Override
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {

        validateRoleCode(request.roleCode(), null);
        validateRoleName(request.roleName(), null);

        Role role = roleMapper.toEntity(request);
        role.setStatus(CommonStatus.ACTIVE);

        Role savedRole = roleRepository.saveAndFlush(role);

        return roleMapper.toResponse(savedRole);
    }

    // Get role by ID
    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID roleId) {

        Role role = findRoleById(roleId);

        return roleMapper.toResponse(role);
    }

    // Get all roles
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {

        return roleMapper.toResponseList(roleRepository.findAll());
    }

    // Update role details
    @Override
    @Transactional
    public RoleResponse updateRole(UUID roleId, UpdateRoleRequest request) {

        Role role = findRoleById(roleId);

        validateRoleCode(request.roleCode(), role);
        validateRoleName(request.roleName(), role);

        roleMapper.updateEntity(request, role);

        Role updatedRole = roleRepository.save(role);

        return roleMapper.toResponse(updatedRole);
    }

    // Update role status
    @Override
    @Transactional
    public RoleResponse updateRoleStatus(
            UUID roleId,
            UpdateRoleStatusRequest request) {

        Role role = findRoleById(roleId);

        if (role.getStatus() == request.status()) {
            throw new ConflictException(
                    ResponseMessage.ROLE_STATUS_ALREADY_UPDATED
            );
        }

        role.setStatus(request.status());

        Role updatedRole = roleRepository.save(role);

        return roleMapper.toResponse(updatedRole);
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

    // Validate unique role code
    private void validateRoleCode(String roleCode, Role existingRole) {

        boolean changed = existingRole == null
                || !existingRole.getRoleCode().equalsIgnoreCase(roleCode);

        if (changed && roleRepository.existsByRoleCode(roleCode)) {
            throw new ConflictException(
                    ResponseMessage.ROLE_CODE_ALREADY_EXISTS
            );
        }
    }

    // Validate unique role name
    private void validateRoleName(String roleName, Role existingRole) {

        boolean changed = existingRole == null
                || !existingRole.getRoleName().equalsIgnoreCase(roleName);

        if (changed && roleRepository.existsByRoleName(roleName)) {
            throw new ConflictException(
                    ResponseMessage.ROLE_NAME_ALREADY_EXISTS
            );
        }
    }
}