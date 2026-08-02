package com.project.milkcollection.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Handles forbidden requests when user lacks required permission.
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // Return custom 403 forbidden response.
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        ApiErrorResponse errorResponse =
                ApiErrorResponse.of(
                        ResponseMessage.ACCESS_DENIED
                );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(errorResponse)
                );
    }
}