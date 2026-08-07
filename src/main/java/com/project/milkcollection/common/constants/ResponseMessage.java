package com.project.milkcollection.common.constants;

//Standard response messages used across the application.
public final class ResponseMessage {

    private ResponseMessage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    //Common
    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";

    public static final String CREATED_SUCCESSFULLY = "created successfully";
    public static final String UPDATED_SUCCESSFULLY = "updated successfully";
    public static final String DELETED_SUCCESSFULLY = "deleted successfully";
    public static final String FETCH_SUCCESSFULLY = "data retrieved successfully";


    // Authentication response messages
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String LOGOUT_SUCCESSFUL = "Logout successful";
    public static final String REFRESH_TOKEN_SUCCESSFUL = "Token refreshed successfully";

    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";

    public static final String ACCOUNT_DISABLED = "User account is disabled";
    public static final String ACCOUNT_LOCKED = "User account is locked";

    public static final String INVALID_TOKEN = "Invalid or expired token";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_REQUIRED = "Authentication token is required";

    public static final String ACCESS_DENIED = "You do not have permission to access this resource";

    // Role response messages
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_NAME_ALREADY_EXISTS = "Role name already exists";
    public static final String ROLE_CODE_ALREADY_EXISTS = "Role code already exists";
    public static final String ROLE_DEACTIVATED_SUCCESSFULLY = "Role deactivated successfully";
    public static final String ROLE_STATUS_ALREADY_UPDATED = "Role already has the requested status";
    public static final String ROLE_ALREADY_DELETED = "Role is already inactive";
}