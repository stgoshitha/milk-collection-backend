package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.systemmodule.CreateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleStatusRequest;
import com.project.milkcollection.auth.dto.response.SystemModuleResponse;
import com.project.milkcollection.auth.entity.SystemModule;
import com.project.milkcollection.auth.mapper.SystemModuleMapper;
import com.project.milkcollection.auth.repository.SystemModuleRepository;
import com.project.milkcollection.auth.service.SystemModuleService;
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
public class SystemModuleServiceImpl implements SystemModuleService {

    private final SystemModuleRepository systemModuleRepository;
    private final SystemModuleMapper systemModuleMapper;

    // Create a new system module
    @Override
    @Transactional
    public SystemModuleResponse createSystemModule(
            CreateSystemModuleRequest createSystemModuleRequest) {

        validateSystemModuleName(createSystemModuleRequest.systemModuleName(), null);

        Integer nextDisplayOrder = getNextDisplayOrder();

        SystemModule systemModule = systemModuleMapper.toEntity(createSystemModuleRequest);

        systemModule.setDisplayOrder(nextDisplayOrder);
        systemModule.setStatus(CommonStatus.ACTIVE);

        SystemModule savedModule = systemModuleRepository.saveAndFlush(systemModule);

        return systemModuleMapper.toResponse(savedModule);
    }

    // Get a system module by ID
    @Override
    @Transactional(readOnly = true)
    public SystemModuleResponse getSystemModuleById(UUID systemModuleId) {

        SystemModule systemModule = findSystemModuleById(systemModuleId);

        return systemModuleMapper.toResponse(systemModule);
    }

    // Get all system modules ordered by display order
    @Override
    @Transactional(readOnly = true)
    public List<SystemModuleResponse> getAllSystemModules() {

        // Fetch all system modules in ascending display order
        List<SystemModule> systemModules =
                systemModuleRepository.findAll(
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.ASC,
                                "displayOrder"
                        )
                );

        return systemModuleMapper.toResponseList(systemModules);
    }

    // Update an existing system module
    @Override
    public SystemModuleResponse updateSystemModule(
            UUID systemModuleId,
            UpdateSystemModuleRequest updateSystemModuleRequest) {

        SystemModule systemModule = findSystemModuleById(systemModuleId);

        validateSystemModuleName(updateSystemModuleRequest.systemModuleName(), systemModule);

        systemModuleMapper.updateEntity(updateSystemModuleRequest, systemModule);

        SystemModule updatedModule = systemModuleRepository.save(systemModule);

        return systemModuleMapper.toResponse(updatedModule);
    }

    // Activate or deactivate a system module
    @Override
    @Transactional
    public SystemModuleResponse updateModuleStatus(
            UUID systemModuleId,
            UpdateSystemModuleStatusRequest request) {

        SystemModule systemModule = findSystemModuleById(systemModuleId);

        if (systemModule.getStatus() == request.status()) {
            throw new ConflictException(
                    ResponseMessage.SYSTEM_MODULE_STATUS_ALREADY_UPDATED
            );
        }

        systemModule.setStatus(request.status());

        SystemModule updatedModule = systemModuleRepository.save(systemModule);

        return systemModuleMapper.toResponse(updatedModule);
    }

    // Find a system module by ID or throw an exception
    private SystemModule findSystemModuleById(UUID systemModuleId) {

        return systemModuleRepository.findById(systemModuleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ResponseMessage.SYSTEM_MODULE_NOT_FOUND
                        )
                );
    }

    // Validate that the system module name is unique
    private void validateSystemModuleName(
            String systemModuleName,
            SystemModule existingSystemModule) {

        boolean changed = existingSystemModule == null
                || !existingSystemModule.getSystemModuleName().equalsIgnoreCase(systemModuleName);

        if (changed && systemModuleRepository.existsBySystemModuleName(systemModuleName)) {
            throw new ConflictException(
                    ResponseMessage.SYSTEM_MODULE_NAME_ALREADY_EXISTS
            );
        }
    }

    // Generate the next available display order
    private Integer getNextDisplayOrder() {

        return systemModuleRepository.findMaxDisplayOrder()
                .orElse(0) + 1;
    }
}