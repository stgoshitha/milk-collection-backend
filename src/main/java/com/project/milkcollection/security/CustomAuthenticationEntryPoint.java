package com.project.milkcollection.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Handles unauthorized requests when authentication fails.
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;


    // Return custom 401 unauthorized response.
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {


        ApiErrorResponse errorResponse =
                ApiErrorResponse.of(
                        ResponseMessage.INVALID_TOKEN
                );


        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");


        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(errorResponse)
                );
    }
}