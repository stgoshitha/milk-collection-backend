package com.project.milkcollection.auth.mapper;

import com.project.milkcollection.auth.dto.response.RolePermissionResponse;
import com.project.milkcollection.auth.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface RolePermissionMapper {

    @Mapping(target = "roleId", source = "role.roleId")
    @Mapping(target = "roleName", source = "role.roleName")
    @Mapping(target = "permissionId", source = "permission.permissionId")
    @Mapping(target = "permissionName", source = "permission.permissionName")
    RolePermissionResponse toResponse(RolePermission rolePermission);

    List<RolePermissionResponse> toResponseList(
            List<RolePermission> rolePermissions
    );
}