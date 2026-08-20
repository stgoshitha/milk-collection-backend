package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.auth.ForgotPasswordRequest;
import com.project.milkcollection.auth.dto.request.auth.LoginRequest;
import com.project.milkcollection.auth.dto.request.auth.RefreshTokenRequest;
import com.project.milkcollection.auth.dto.request.auth.ResetPasswordRequest;
import com.project.milkcollection.auth.dto.response.LoginResponse;
import com.project.milkcollection.auth.dto.response.RefreshTokenResponse;
import com.project.milkcollection.auth.entity.RefreshToken;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.auth.repository.RefreshTokenRepository;
import com.project.milkcollection.auth.repository.UserRepository;
import com.project.milkcollection.auth.service.AuthService;
import com.project.milkcollection.auth.service.PasswordResetTokenService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.common.service.EmailService;
import com.project.milkcollection.exception.UnauthorizedException;
import com.project.milkcollection.exception.ValidationException;
import com.project.milkcollection.security.JwtService;
import com.project.milkcollection.security.RefreshTokenHashService;
import com.project.milkcollection.security.SecureTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final RefreshTokenHashService refreshTokenHashService;
    private final SecureTokenService secureTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public LoginResponse login(
            LoginRequest loginRequest,
            String ipAddress,
            String deviceInfo
    ) {

        // Find user using username OR email.
        User user =
                userRepository
                        .findByUsernameOrEmail(
                                loginRequest.loginIdentifier(),
                                loginRequest.loginIdentifier()
                        )
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_CREDENTIALS
                                )
                        );

        Authentication authentication;

        try {

            // Authenticate username + password.
            authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    loginRequest.password()
                            )
                    );

        } catch (BadCredentialsException exception) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CREDENTIALS
            );
        }

        // Authentication must be successful.
        if (!authentication.isAuthenticated()) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CREDENTIALS
            );
        }

        // User must be active.
        if (user.getStatus() != UserStatus.ACTIVE) {

            throw new UnauthorizedException(
                    ResponseMessage.ACCOUNT_NOT_ACTIVE
            );
        }

        // Generate access token.
        String accessToken =
                jwtService.generateAccessToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );

        // Generate refresh token.
        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUserId()
                );

        // Hash refresh token before storing.
        String tokenHash =
                refreshTokenHashService.hash(
                        refreshToken
                );

        // Create refresh-token session.
        RefreshToken refreshTokenEntity =
                RefreshToken.builder()
                        .user(user)
                        .tokenHash(tokenHash)
                        .ipAddress(ipAddress)
                        .deviceInfo(deviceInfo)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plus(
                                                Duration.ofMillis(
                                                        SecurityConstants
                                                                .REFRESH_TOKEN_EXPIRATION
                                                )
                                        )
                        )
                        .build();

        refreshTokenRepository.save(
                refreshTokenEntity
        );

        // Update last login.
        user.setLastLogin(
                LocalDateTime.now()
        );

        userRepository.save(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                SecurityConstants.TOKEN_TYPE,
                SecurityConstants.ACCESS_TOKEN_EXPIRATION
        );
    }


    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(
            RefreshTokenRequest refreshTokenRequest
    ) {

        String refreshToken =
                refreshTokenRequest.refreshToken();

        // Validate JWT refresh token.
        if (!jwtService.isRefreshTokenValid(refreshToken)) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        // Hash received refresh token.
        String tokenHash =
                refreshTokenHashService.hash(
                        refreshToken
                );

        // Find token in database.
        RefreshToken storedToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_REFRESH_TOKEN
                                )
                        );

        // Check whether token has already been revoked.
        if (storedToken.getRevokedAt() != null) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        // Check database expiration.
        if (storedToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new UnauthorizedException(
                    ResponseMessage.REFRESH_TOKEN_EXPIRED
            );
        }

        User user =
                storedToken.getUser();

        // User must still be active.
        if (user.getStatus() != UserStatus.ACTIVE) {

            throw new UnauthorizedException(
                    ResponseMessage.ACCOUNT_NOT_ACTIVE
            );
        }

        // Preserve device information.
        String deviceInfo =
                storedToken.getDeviceInfo();

        // Preserve IP address.
        String ipAddress =
                storedToken.getIpAddress();

        // Revoke old refresh token.
        storedToken.setRevokedAt(
                LocalDateTime.now()
        );

        // Generate new access token.
        String newAccessToken =
                jwtService.generateAccessToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );

        // Generate new refresh token.
        String newRefreshToken =
                jwtService.generateRefreshToken(
                        user.getUserId()
                );

        // Hash new refresh token.
        String newTokenHash =
                refreshTokenHashService.hash(
                        newRefreshToken
                );

        // Save new refresh-token session.
        RefreshToken newRefreshTokenEntity =
                RefreshToken.builder()
                        .user(user)
                        .tokenHash(newTokenHash)
                        .deviceInfo(deviceInfo)
                        .ipAddress(ipAddress)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plus(
                                                Duration.ofMillis(
                                                        SecurityConstants
                                                                .REFRESH_TOKEN_EXPIRATION
                                                )
                                        )
                        )
                        .build();

        refreshTokenRepository.save(
                newRefreshTokenEntity
        );

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                SecurityConstants.TOKEN_TYPE,
                SecurityConstants.ACCESS_TOKEN_EXPIRATION
        );
    }


    @Override
    @Transactional
    public void logout(
            RefreshTokenRequest refreshTokenRequest
    ) {

        String refreshToken =
                refreshTokenRequest.refreshToken();

        // Validate refresh JWT.
        if (!jwtService.isRefreshTokenValid(refreshToken)) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        // Hash token.
        String tokenHash =
                refreshTokenHashService.hash(
                        refreshToken
                );

        // Find token in database.
        RefreshToken storedRefreshToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_REFRESH_TOKEN
                                )
                        );

        // Check whether token was already revoked.
        if (storedRefreshToken.getRevokedAt() != null) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        // Revoke token.
        storedRefreshToken.setRevokedAt(
                LocalDateTime.now()
        );

        refreshTokenRepository.save(
                storedRefreshToken
        );
    }


    @Override
    @Transactional
    public void forgotPassword(
            ForgotPasswordRequest forgotPasswordRequest
    ) {

        userRepository
                .findByUsernameOrEmail(
                        forgotPasswordRequest.loginIdentifier(),
                        forgotPasswordRequest.loginIdentifier()
                )
                .ifPresent(user -> {

                    String token =
                            secureTokenService.generateToken();

                    passwordResetTokenService.saveToken(
                            user,
                            token
                    );

                    emailService.sendPasswordResetEmail(
                            user.getEmail(),
                            token
                    );
                });
    }


    @Override
    @Transactional
    public void resetPassword(
            String resetToken,
            ResetPasswordRequest resetPasswordRequest
    ) {

        // Confirm both passwords are equal.
        if (!resetPasswordRequest.newPassword()
                .equals(
                        resetPasswordRequest.confirmPassword()
                )) {

            throw new ValidationException(
                    ResponseMessage.PASSWORD_MISMATCH
            );
        }

        // Validate reset token.
        User user =
                passwordResetTokenService.validateToken(resetToken);

        // Update password.
        user.setPassword(
                passwordEncoder.encode(
                        resetPasswordRequest.newPassword()
                )
        );

        userRepository.save(user);

        // Mark reset token as used.
        passwordResetTokenService.markTokenAsUsed(resetToken);

        /*
         * Important:
         * Password reset should invalidate all existing
         * refresh-token sessions.
         */
        refreshTokenRepository.revokeAllByUser(user);
    }
}