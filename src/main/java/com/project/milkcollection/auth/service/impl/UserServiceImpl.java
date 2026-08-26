package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.user.*;
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
import com.project.milkcollection.common.file.dto.FileUploadResponse;
import com.project.milkcollection.common.file.service.FileStorageService;
import com.project.milkcollection.common.file.validator.ImageFileValidator;
import com.project.milkcollection.exception.ConflictException;
import com.project.milkcollection.exception.ResourceNotFoundException;
import com.project.milkcollection.exception.UnauthorizedException;
import com.project.milkcollection.exception.ValidationException;
import com.project.milkcollection.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final ImageFileValidator imageFileValidator;

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
    public UserResponse updateUser(
            UUID userId,
            UpdateUserRequest updateUserRequest)
    {

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

    @Override
    @Transactional
    public UserResponse updateUserStatus(
            UUID userId,
            UpdateUserStatusRequest updateUserStatusRequest)
    {
        User existingUser = findByUserId(userId);

        validateStatusTransition(
                existingUser.getStatus(),
                updateUserStatusRequest.status()
        );

        existingUser.setStatus(updateUserStatusRequest.status());

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {

        UUID userId = SecurityUtils.getAuthenticatedUserId();

        User user =  findByUserId(userId);

        if(!passwordEncoder.matches(
                changePasswordRequest.currentPassword()
                ,user.getPassword()
        )){
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CURRENT_PASSWORD
            );
        }

        if(!changePasswordRequest.newPassword()
                .equals(changePasswordRequest.confirmPassword()
                )
        ){
            throw new ValidationException(
                    ResponseMessage.PASSWORD_MISMATCH
            );
        }

        if(passwordEncoder.matches(
                changePasswordRequest.newPassword(),
                user.getPassword()
        )){
            throw new ConflictException(
                    ResponseMessage.SAME_PASSWORD
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        changePasswordRequest.newPassword()
                )
        );

        userRepository.save(user);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {

        UUID currentUserId = SecurityUtils.getAuthenticatedUserId();

        User currentUser = findByUserId(currentUserId);

        return userMapper.toResponse(currentUser);
    }

    @Override
    @Transactional
    public UserResponse updateProfileImage(UpdateProfileImageRequest updateProfileImageRequest) {

        UUID userId = SecurityUtils.getAuthenticatedUserId();

        User user = findByUserId(userId);

        String oldProfileImageUrl = user.getProfileImgUrl();

        MultipartFile newProfileImage = updateProfileImageRequest.profile();

        imageFileValidator.validate(newProfileImage);

        FileUploadResponse uploadResponse  =
                fileStorageService.upload(
                        newProfileImage,
                        "profile-image"
                );

        user.setProfileImgUrl(uploadResponse.url());
        user.setProfileImageKey(uploadResponse.publicID());

        User updatedUser = userRepository.save(user);

        if (oldProfileImageUrl != null
                && !oldProfileImageUrl.isBlank()) {

            fileStorageService.delete(
                    user.getProfileImageKey()
            );
        }

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteProfileImage() {

        UUID userId = SecurityUtils.getAuthenticatedUserId();

        User user = findByUserId(userId);

        String profileImageKey = user.getProfileImageKey();

        if (profileImageKey == null || profileImageKey.isBlank()) {
            throw new ResourceNotFoundException(
                    ResponseMessage.PROFILE_IMAGE_NOT_FOUND
            );
        }

        fileStorageService.delete(profileImageKey);

        user.setProfileImgUrl(null);
        user.setProfileImageKey(null);

        userRepository.save(user);
    }

    // ------------------ Private Methods ------------------

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

    // Validate user status transition
    private void validateStatusTransition(
            UserStatus currentStatus,
            UserStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new ConflictException(
                    ResponseMessage.USER_ALREADY_IN_STATUS
            );
        }
    }

}
