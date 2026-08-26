package com.project.milkcollection.auth.mapper;

import com.project.milkcollection.auth.dto.request.systemmodule.CreateSystemModuleRequest;
import com.project.milkcollection.auth.dto.request.systemmodule.UpdateSystemModuleRequest;
import com.project.milkcollection.auth.dto.response.SystemModuleResponse;
import com.project.milkcollection.auth.entity.SystemModule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface SystemModuleMapper {

    // Convert create request to System Module entity
    @Mapping(target = "systemModuleId", ignore = true)
    @Mapping(target = "displayOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SystemModule toEntity(CreateSystemModuleRequest createSystemModuleRequest);

    // Convert System Module entity to response DTO
    SystemModuleResponse toResponse(SystemModule systemModule);

    // Convert System Module entity list to response DTO list
    List<SystemModuleResponse> toResponseList(List<SystemModule> systemModules);

    // Update existing System Module entity from update request
    @Mapping(target = "systemModuleId", ignore = true)
    @Mapping(target = "displayOrder", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateSystemModuleRequest updateSystemModuleRequest, @MappingTarget SystemModule systemModule);
}
