package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.systemmodule.CreateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleOrderRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleStatusRequest;
import com.project.milkcollection.auth.dto.response.SystemModuleResponse;

import java.util.List;
import java.util.UUID;

public interface SystemModuleService {

    SystemModuleResponse createSystemModule(
            CreateSystemModuleRequest createSystemModuleRequest
    );

    SystemModuleResponse getSystemModuleById(
            UUID systemModuleId
    );

    List<SystemModuleResponse> getAllSystemModules();

    SystemModuleResponse updateSystemModule(
            UUID systemModuleId,
            UpdateSystemModuleRequest updateSystemModuleRequest
    );

    SystemModuleResponse updateModuleStatus(
            UUID systemModuleId,
            UpdateSystemModuleStatusRequest updateSystemModuleStatusRequest
    );

    void updateSystemModuleOrder(
            UpdateSystemModuleOrderRequest updateSystemModuleOrderRequest
    );
}