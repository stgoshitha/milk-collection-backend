package com.project.milkcollection.auth.mapper;

import com.project.milkcollection.auth.dto.request.CreateRoleRequest;
import com.project.milkcollection.auth.dto.response.RoleResponse;
import com.project.milkcollection.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface RoleMapper {

    // Convert create request DTO to Role entity
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(CreateRoleRequest request);


    // Convert Role entity to response DTO
    RoleResponse toResponse(Role role);


    // Convert list of Role entities to response DTO list
    List<RoleResponse> toResponseList(List<Role> roles);

}