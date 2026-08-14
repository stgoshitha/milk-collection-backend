package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.dto.request.auth.LoginRequest;
import com.project.milkcollection.auth.dto.request.auth.RefreshTokenRequest;
import com.project.milkcollection.auth.dto.response.LoginResponse;
import com.project.milkcollection.auth.dto.response.RefreshTokenResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest, String ipAddress, String deviceInfo);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);

}