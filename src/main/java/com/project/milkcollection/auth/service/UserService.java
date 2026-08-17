package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.user.ChangePasswordRequest;
import com.project.milkcollection.auth.dto.request.user.CreateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserStatusRequest;
import com.project.milkcollection.auth.dto.response.UserResponse;
import com.project.milkcollection.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse getUserById(UUID userId);

    PageResponse<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest);

    UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest updateUserStatusRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    UserResponse getCurrentUser();

}
