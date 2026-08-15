package com.project.milkcollection.auth.mapper;

import com.project.milkcollection.auth.dto.request.user.CreateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserRequest;
import com.project.milkcollection.auth.dto.response.UserResponse;
import com.project.milkcollection.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "profileImgUrl", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserRequest createUserRequest);

    @Mapping(target = "roleId", source = "role.roleId")
    @Mapping(target = "roleName", source = "role.roleName")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "profileImgUrl", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
