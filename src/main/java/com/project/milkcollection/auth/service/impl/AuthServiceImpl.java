package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.auth.LoginRequest;
import com.project.milkcollection.auth.dto.request.auth.RefreshTokenRequest;
import com.project.milkcollection.auth.dto.response.LoginResponse;
import com.project.milkcollection.auth.dto.response.RefreshTokenResponse;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.auth.repository.UserRepository;
import com.project.milkcollection.auth.service.AuthService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.exception.InvalidCredentialsException;
import com.project.milkcollection.exception.UnauthorizedException;
import com.project.milkcollection.security.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        // Find user using username OR email
        User user = userRepository
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

            // Authenticate username and password
            authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    loginRequest.password()
                            )
                    );

        } catch (InvalidCredentialsException exception) {

            // Invalid password
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CREDENTIALS
            );
        }

        // Make sure authentication was successful
        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CREDENTIALS
            );
        }

        // Generate access token
        String accessToken =
                jwtService.generateAccessToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );

        // Generate refresh token
        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUserId()
                );

        // Update last login time
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                SecurityConstants.TOKEN_TYPE,
                SecurityConstants.ACCESS_TOKEN_EXPIRATION
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshTokenResponse refreshToken(
            RefreshTokenRequest refreshTokenRequest
    ) {

        try {

            String refreshToken = refreshTokenRequest.refreshToken();

            // Make sure token is actually a refresh token.
            String tokenType =
                    jwtService.extractTokenType(refreshToken);

            if (!SecurityConstants.REFRESH_TOKEN.equals(tokenType)) {
                throw new UnauthorizedException(
                        ResponseMessage.INVALID_REFRESH_TOKEN
                );
            }

            // Extract user ID from refresh token.
            UUID userId =
                    jwtService.extractUserId(refreshToken);

            // Validate token expiration/signature.
            if (jwtService.isTokenExpired(refreshToken)) {
                throw new UnauthorizedException(
                        ResponseMessage.REFRESH_TOKEN_EXPIRED
                );
            }

            User user =
                    userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new UnauthorizedException(
                                            ResponseMessage.INVALID_REFRESH_TOKEN
                                    )
                            );

            // User must still be active.
            if (user.getStatus() != UserStatus.ACTIVE) {
                throw new UnauthorizedException(
                        ResponseMessage.ACCOUNT_NOT_ACTIVE
                );
            }

            // Generate new access token.
            String newAccessToken =
                    jwtService.generateAccessToken(
                            user.getUserId(),
                            user.getUsername(),
                            user.getRole().getRoleName()
                    );

            // Rotate refresh token.
            String newRefreshToken =
                    jwtService.generateRefreshToken(
                            user.getUserId()
                    );

            return new RefreshTokenResponse(
                    newAccessToken,
                    newRefreshToken,
                    SecurityConstants.TOKEN_TYPE,
                    SecurityConstants.ACCESS_TOKEN_EXPIRATION
            );

        } catch (JwtException | IllegalArgumentException exception) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }
    }
}