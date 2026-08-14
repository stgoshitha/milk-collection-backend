package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.dto.request.auth.LoginRequest;
import com.project.milkcollection.auth.dto.request.auth.RefreshTokenRequest;
import com.project.milkcollection.auth.dto.response.LoginResponse;
import com.project.milkcollection.auth.dto.response.RefreshTokenResponse;
import com.project.milkcollection.auth.entity.RefreshToken;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.auth.repository.RefreshTokenRepository;
import com.project.milkcollection.auth.repository.UserRepository;
import com.project.milkcollection.auth.service.AuthService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.exception.UnauthorizedException;
import com.project.milkcollection.security.JwtService;
import com.project.milkcollection.security.RefreshTokenHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest, String ipAddress, String deviceInfo) {

        //Find user by username or email.
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

        //Authenticate username + password.
        Authentication authentication;

        try {
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

        //Authentication must be successful.
        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_CREDENTIALS
            );
        }

        //User must be active.
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException(
                    ResponseMessage.ACCOUNT_NOT_ACTIVE
            );
        }

        //Generate access token
        String accessToken =
                jwtService.generateAccessToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );

        //Generate refresh token
        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUserId()
                );

        //Hash refresh token.
        String tokenHash =
                refreshTokenHashService.hash(
                        refreshToken
                );

        //Save refresh token session
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

        refreshTokenRepository.save(refreshTokenEntity);

        //Update last login.
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        //Return tokens.
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

        String refreshToken = refreshTokenRequest.refreshToken();

        //Validate refresh JWT
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        //Hash received token
        String tokenHash =
                refreshTokenHashService.hash(
                        refreshToken
                );

        //Find token in database
        RefreshToken storedToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_REFRESH_TOKEN
                                )
                        );

        //Check whether token has already been revoked.
        if (storedToken.getRevokedAt() != null) {
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        //Check database expiration
        if (storedToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new UnauthorizedException(
                    ResponseMessage.REFRESH_TOKEN_EXPIRED
            );
        }

        //Get user
        User user = storedToken.getUser();

        //User must still be active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException(
                    ResponseMessage.ACCOUNT_NOT_ACTIVE
            );
        }

        //Get the existing information
        String deviceInfo = storedToken.getDeviceInfo();
        String ipAddress = storedToken.getIpAddress();

        //Revoke old refresh token
        storedToken.setRevokedAt(LocalDateTime.now());

        //Generate new access token
        String newAccessToken =
                jwtService.generateAccessToken(
                        user.getUserId(),
                        user.getUsername(),
                        user.getRole().getRoleName()
                );

        //Generate new refresh token.
        String newRefreshToken =
                jwtService.generateRefreshToken(
                        user.getUserId()
                );

        //Hash new refresh token
        String newTokenHash =
                refreshTokenHashService.hash(
                        newRefreshToken
                );

        //Save new refresh token.
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

        refreshTokenRepository.save(newRefreshTokenEntity);

        //Return new tokens
        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                SecurityConstants.TOKEN_TYPE,
                SecurityConstants.ACCESS_TOKEN_EXPIRATION
        );
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest refreshTokenRequest) {

        String  refreshToken = refreshTokenRequest.refreshToken();

        //Validate that the supplied token is actually a valid refresh token
        if(!jwtService.isRefreshTokenValid(refreshToken)){
            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        //Hash the token
        String tokenHash = refreshTokenHashService.hash(refreshToken);

        //find the token from DB
        RefreshToken storedRefreshToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new UnauthorizedException(
                                ResponseMessage.INVALID_REFRESH_TOKEN
                        )
                );

        //check whether the token is already revoked
        if (storedRefreshToken.getRevokedAt() != null) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_REFRESH_TOKEN
            );
        }

        //revoke the token
        storedRefreshToken.setRevokedAt(LocalDateTime.now());

        refreshTokenRepository.save(storedRefreshToken);

    }
}