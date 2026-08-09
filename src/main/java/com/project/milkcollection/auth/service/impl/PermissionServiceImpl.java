package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.permission.CreatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionStatusRequest;
import com.project.milkcollection.auth.dto.response.PermissionResponse;
import com.project.milkcollection.auth.entity.Permission;
import com.project.milkcollection.auth.entity.SystemModule;
import com.project.milkcollection.auth.mapper.PermissionMapper;
import com.project.milkcollection.auth.repository.PermissionRepository;
import com.project.milkcollection.auth.repository.SystemModuleRepository;
import com.project.milkcollection.auth.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final SystemModuleRepository systemModuleRepository;
    private final PermissionMapper permissionMapper;

    // Create a new permission
    @Override
    @Transactional
    public PermissionResponse createPermission(
            CreatePermissionRequest createPermissionRequest) {

        validatePermissionName(
                createPermissionRequest.permissionName(),
                null
        );

        SystemModule systemModule = findSystemModuleById(
                createPermissionRequest.systemModuleId()
        );

        Permission permission = permissionMapper.toEntity(
                createPermissionRequest
        );

        permission.setSystemModule(systemModule);
        permission.setStatus(CommonStatus.ACTIVE);

        Permission savedPermission = permissionRepository.saveAndFlush(
                permission
        );

        return permissionMapper.toResponse(savedPermission);
    }

    // Get permission by ID
    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(UUID permissionId) {

        Permission permission = findPermissionById(permissionId);

        return permissionMapper.toResponse(permission);
    }

    // Get all permissions
    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {

        return permissionMapper.toResponseList(
                permissionRepository.findAll()
        );
    }

    // Update permission details
    @Override
    @Transactional
    public PermissionResponse updatePermission(
            UUID permissionId,
            UpdatePermissionRequest updatePermissionRequest) {

        Permission permission = findPermissionById(permissionId);

        validatePermissionName(
                updatePermissionRequest.permissionName(),
                permission
        );

        SystemModule systemModule = findSystemModuleById(
                updatePermissionRequest.systemModuleId()
        );

        permissionMapper.updateEntity(
                updatePermissionRequest,
                permission
        );

        permission.setSystemModule(systemModule);

        Permission updatedPermission = permissionRepository.save(
                permission
        );

        return permissionMapper.toResponse(updatedPermission);
    }

    // Update permission status
    @Override
    @Transactional
    public PermissionResponse updatePermissionStatus(
            UUID permissionId,
            UpdatePermissionStatusRequest updatePermissionStatusRequest) {

        Permission permission = findPermissionById(permissionId);

        if (permission.getStatus()
                == updatePermissionStatusRequest.status()) {

            throw new ConflictException(
                    ResponseMessage.PERMISSION_STATUS_ALREADY_UPDATED
            );
        }

        permission.setStatus(updatePermissionStatusRequest.status());

        Permission updatedPermission = permissionRepository.save(
                permission
        );

        return permissionMapper.toResponse(updatedPermission);
    }

    // Get all permissions belonging to a system module
    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsBySystemModule(UUID systemModuleId) {

        // Check whether the system module exists
        if (!systemModuleRepository.existsById(systemModuleId)) {
            throw new ResourceNotFoundException(
                    ResponseMessage.MODULE_NOT_FOUND
            );
        }

        // Find permissions belonging to the module
        List<Permission> permissions =
                permissionRepository.findBySystemModuleSystemModuleId(systemModuleId);

        return permissionMapper.toResponseList(permissions);
    }

    // Find permission by ID or throw exception
    private Permission findPermissionById(UUID permissionId) {

        return permissionRepository.findById(permissionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ResponseMessage.PERMISSION_NOT_FOUND
                        )
                );
    }

    // Find system module by ID or throw exception
    private SystemModule findSystemModuleById(UUID systemModuleId) {

        return systemModuleRepository.findById(systemModuleId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ResponseMessage.MODULE_NOT_FOUND
                        )
                );
    }

    // Validate unique permission name
    private void validatePermissionName(
            String permissionName,
            Permission existingPermission) {

        boolean changed =
                existingPermission == null
                        || !existingPermission
                        .getPermissionName()
                        .equalsIgnoreCase(permissionName);

        if (changed
                && permissionRepository
                .existsByPermissionName(permissionName)) {

            throw new ConflictException(
                    ResponseMessage.PERMISSION_NAME_ALREADY_EXISTS
            );
        }
    }
}