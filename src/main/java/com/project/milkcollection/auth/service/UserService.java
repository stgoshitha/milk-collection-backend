package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.user.CreateUserRequest;
import com.project.milkcollection.auth.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest createUserRequest);

}
