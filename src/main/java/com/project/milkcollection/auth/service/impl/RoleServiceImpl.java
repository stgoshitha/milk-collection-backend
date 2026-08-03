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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    // Create new role
    @Override
    public RoleResponse createRole(CreateRoleRequest request) {

        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new ConflictException(ResponseMessage.ROLE_ALREADY_EXISTS);
        }

        Role role = roleMapper.toEntity(request);

        role.setStatus(CommonStatus.ACTIVE);

        Role savedRole = roleRepository.saveAndFlush(role);

        return roleMapper.toResponse(savedRole);
    }
}
