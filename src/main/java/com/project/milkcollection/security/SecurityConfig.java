package com.project.milkcollection.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Main Spring Security configuration.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    // Configure security rules and JWT authentication.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http

                // Disable CSRF because JWT authentication is stateless.
                .csrf(csrf -> csrf.disable())

                // Disable session-based authentication.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Configure authentication and authorization exceptions.
                .exceptionHandling(exception ->
                        exception
                                // 401 - User is not authenticated.
                                .authenticationEntryPoint(
                                        authenticationEntryPoint
                                )

                                // 403 - User is authenticated but
                                // does not have sufficient permissions.
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )

                // Define public and protected endpoints.
                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints.
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/reset-password"
                        ).permitAll()

                        // Every other endpoint requires authentication.
                        .anyRequest()
                        .authenticated()
                )

                // Add JWT filter before username/password authentication.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}