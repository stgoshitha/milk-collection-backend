package com.project.milkcollection.common.constants;

//Security related constants used throughout the application.
public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // JWT Claims
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_PERMISSIONS = "permissions";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";

    // Authentication Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";

    // Authentication Endpoints
    public static final String AUTH_BASE_URL = "/api/v1/auth";

    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REFRESH_ENDPOINT = "/refresh";
    public static final String LOGOUT_ENDPOINT = "/logout";

    // Security Context
    public static final String ANONYMOUS_USER = "anonymousUser";

    // Roles
    public static final String ROLE_PREFIX = "ROLE_";

    // Password
    public static final int BCRYPT_STRENGTH = 12;

}