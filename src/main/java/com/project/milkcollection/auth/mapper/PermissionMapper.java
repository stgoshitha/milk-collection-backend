package com.project.milkcollection.auth.mapper;

import com.project.milkcollection.auth.dto.request.permission.CreatePermissionRequest;
import com.project.milkcollection.auth.dto.request.permission.UpdatePermissionRequest;
import com.project.milkcollection.auth.dto.response.PermissionResponse;
import com.project.milkcollection.auth.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface PermissionMapper {

    // Convert create request DTO to Permission entity
    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "systemModule", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Permission toEntity(CreatePermissionRequest createPermissionRequest);

    // Convert Permission entity to response DTO
    @Mapping(target = "systemModuleId", source = "systemModule.systemModuleId")
    @Mapping(target = "systemModuleName", source = "systemModule.systemModuleName")
    PermissionResponse toResponse(Permission permission);

    // Convert list of Permission entities to response DTO list
    List<PermissionResponse> toResponseList(List<Permission> permissions);

    // Update existing Permission entity from update request
    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "systemModule", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            UpdatePermissionRequest updatePermissionRequest,
            @MappingTarget Permission permission
    );

}