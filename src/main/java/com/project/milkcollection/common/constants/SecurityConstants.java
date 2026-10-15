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
    public static final String CLAIM_USERNAME = "username";

    // Authentication Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";
    public static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRATION = 30L * 24 * 60 * 60 * 1000;
    public static final String CLAIM_TOKEN_TYPE = "token_type";
    public static final String ACCESS_TOKEN = "ACCESS";
    public static final String REFRESH_TOKEN = "REFRESH";


    // Authentication Endpoints
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REFRESH_ENDPOINT = "/refresh";
    public static final String LOGOUT_ENDPOINT = "/logout";

    // Security Context
    public static final String ANONYMOUS_USER = "anonymousUser";

    // Roles
    public static final String ROLE_PREFIX = "ROLE_";

    // Password
    public static final int BCRYPT_STRENGTH = 12;

    // Base URL
    public static final String AUTH_BASE_URL = "/api/v1/auth";
    public static final String USER_BASE_URL = "/api/v1/users";
    public static final String ROLE_BASE_URL = "/api/v1/roles";
    public static final String SYSTEM_MODULE_BASE_URL = "/api/v1/system-modules";
    public static final String PERMISSION_BASE_URL = "/api/v1/permissions";


}