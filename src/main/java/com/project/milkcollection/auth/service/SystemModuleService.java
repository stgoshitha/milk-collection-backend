package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.CreateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.UpdateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.UpdateSystemModuleStatusRequest;
import com.project.milkcollection.auth.dto.response.SystemModuleResponse;

import java.util.List;
import java.util.UUID;

public interface SystemModuleService {

    SystemModuleResponse createSystemModule(CreateSystemModuleRequest createSystemModuleRequest);

    SystemModuleResponse getSystemModuleById(UUID systemModuleId);

    List<SystemModuleResponse> getAllSystemModules();

    SystemModuleResponse updateSystemModule(UUID systemModuleId, UpdateSystemModuleRequest updateSystemModuleRequest);

    SystemModuleResponse updateModuleStatus(UUID systemModuleId, UpdateSystemModuleStatusRequest request);

}
