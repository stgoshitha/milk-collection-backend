package com.project.milkcollection.auth.controller;

import com.project.milkcollection.auth.dto.request.auth.LoginRequest;
import com.project.milkcollection.auth.dto.request.auth.RefreshTokenRequest;
import com.project.milkcollection.auth.dto.response.LoginResponse;
import com.project.milkcollection.auth.dto.response.RefreshTokenResponse;
import com.project.milkcollection.auth.service.AuthService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.constants.SecurityConstants;
import com.project.milkcollection.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SecurityConstants.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //Authenticate user using username or email.
    @PostMapping(SecurityConstants.LOGIN_ENDPOINT)
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse =
                authService.login(loginRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ResponseMessage.LOGIN_SUCCESSFULLY,
                        loginResponse
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {

        RefreshTokenResponse response =
                authService.refreshToken(refreshTokenRequest);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ResponseMessage.TOKEN_REFRESH_SUCCESSFULLY,
                        response
                )
        );
    }

}