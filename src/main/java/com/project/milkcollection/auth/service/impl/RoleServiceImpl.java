package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
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

    // Create new role
    @Override
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {

        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new ConflictException(ResponseMessage.ROLE_ALREADY_EXISTS);
        }

        Role role = roleMapper.toEntity(request);

        role.setStatus(CommonStatus.ACTIVE);

        Role savedRole = roleRepository.saveAndFlush(role);

        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID roleId) {

        Role role = findRoleById(roleId);

        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        return roleMapper.toResponseList(roles);

    }

    private Role findRoleById(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(ResponseMessage.ROLE_NOT_FOUND)
                );
    }
}
