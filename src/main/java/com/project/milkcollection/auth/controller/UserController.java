package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.user.CreateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserStatusRequest;
import com.project.milkcollection.auth.dto.response.UserResponse;
import com.project.milkcollection.auth.service.UserService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.common.dto.PageResponse;
import com.project.milkcollection.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(SecurityConstants.USER_BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {

        UserResponse user = userService.createUser(createUserRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "User " + ResponseMessage.CREATED_SUCCESSFULLY,
                                user
                        )
                );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_GET')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable UUID userId
    ){

        UserResponse user = userService.getUserById(userId);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(
                        "User" + ResponseMessage.FETCH_SUCCESSFULLY,
                        user
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_LIST')")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ){

        PageResponse<UserResponse> users = userService.getAllUsers(pageable);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(
                        "Users " + ResponseMessage.FETCH_SUCCESSFULLY,
                        users
                        )
                );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ){

        UserResponse updatedUser = userService.updateUser(userId, updateUserRequest);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(
                        "User " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        updatedUser
                        )
                );

    }

    @PatchMapping("/{userId}/user-status")
    @PreAuthorize("hasAuthority('USER_STATUS_UPDATE')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest updateUserStatusRequest
    ){

        UserResponse user = userService.updateUserStatus(userId, updateUserStatusRequest);

        return ResponseEntity
                .ok()
                .body(ApiResponse.success(
                        "User status " + ResponseMessage.UPDATED_SUCCESSFULLY,
                        user
                        )
                );
    }
}
