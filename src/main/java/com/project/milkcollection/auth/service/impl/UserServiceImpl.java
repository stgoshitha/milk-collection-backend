package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.user.CreateUserRequest;
import com.project.milkcollection.auth.dto.request.user.UpdateUserRequest;
import com.project.milkcollection.auth.dto.response.UserResponse;
import com.project.milkcollection.auth.entity.Role;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.auth.mapper.UserMapper;
import com.project.milkcollection.auth.repository.RoleRepository;
import com.project.milkcollection.auth.repository.UserRepository;
import com.project.milkcollection.auth.service.UserService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.dto.PageResponse;
import com.project.milkcollection.exception.ConflictException;
import com.project.milkcollection.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {

        validateUsername(createUserRequest.username());
        validateEmail(createUserRequest.email());

        Role role =
                findRoleById(createUserRequest.roleId());

        User user =
                userMapper.toEntity(createUserRequest);

        user.setPassword(
                passwordEncoder.encode(createUserRequest.password())
        );

        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.saveAndFlush(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId) {

        User user = findByUserId(userId);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {

        Page<UserResponse> page = userRepository
                .findAll(pageable)
                .map(userMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) {

        User existingUser = findByUserId(userId);

        validateEmailForUpdate(updateUserRequest.email(), userId);
        validateUsernameForUpdate(updateUserRequest.username(), userId);

        Role role =  findRoleById(updateUserRequest.roleId());

        userMapper.updateEntity(
                updateUserRequest,
                existingUser
        );

        existingUser.setRole(role);

        User updatedUser =
                userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    // Validate username uniqueness
    private void validateUsername(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException(
                    ResponseMessage.USERNAME_ALREADY_EXISTS
            );
        }
    }

    // Validate email uniqueness
    private void validateEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    ResponseMessage.EMAIL_ALREADY_EXISTS
            );
        }
    }

    // Find role by ID or throw exception
    private Role findRoleById(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ResponseMessage.ROLE_NOT_FOUND
                        )
                );
    }

    private User findByUserId(UUID userId){

        return userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                ResponseMessage.USER_NOT_FOUND
                        )
                );
    }

    // Validate username uniqueness when updating the user
    private void validateUsernameForUpdate(
            String username,
            UUID userId
    ) {

        if (userRepository.existsByUsernameAndUserIdNot(username, userId)) {
            throw new ConflictException(
                    ResponseMessage.USERNAME_ALREADY_EXISTS
            );
        }
    }

    // Validate email uniqueness when updating the user
    private void validateEmailForUpdate(
            String email,
            UUID userId
    ) {

        if (userRepository.existsByEmailAndUserIdNot(email, userId)) {
            throw new ConflictException(
                    ResponseMessage.EMAIL_ALREADY_EXISTS
            );
        }
    }

}
