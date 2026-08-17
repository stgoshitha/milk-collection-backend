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
    public static final String LOGIN_SUCCESSFULLY = "Login successful";
    public static final String LOGOUT_SUCCESSFULLY = "Logout successful";
    public static final String TOKEN_REFRESH_SUCCESSFULLY = "Token refreshed successfully";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String INVALID_TOKEN = "Invalid or expired token";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired";
    public static final String TOKEN_REQUIRED = "Authentication token is required";
    public static final String ACCESS_DENIED = "You do not have permission to access this resource";
    public static final String UNAUTHORIZED = "Authentication is required to access this resource.";

    // User response messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USER_ALREADY_IN_STATUS = "User is already in the requested status";
    public static final String ACCOUNT_NOT_ACTIVE = "User account is not active";
    public static final String ACCOUNT_DISABLED = "User account is disabled";
    public static final String ACCOUNT_LOCKED = "User account is locked";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String INVALID_CURRENT_PASSWORD = "Current password is incorrect.";
    public static final String PASSWORD_MISMATCH = "New password and confirm password do not match.";
    public static final String SAME_PASSWORD = "New password must be different from the current password.";
    public static final String PASSWORD_CHANGED = "Password changed successfully.";


    // Role response messages
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_NAME_ALREADY_EXISTS = "Role name already exists";
    public static final String ROLE_CODE_ALREADY_EXISTS = "Role code already exists";
    public static final String ROLE_DEACTIVATED_SUCCESSFULLY = "Role deactivated successfully";
    public static final String ROLE_STATUS_ALREADY_UPDATED = "Role already has the requested status";
    public static final String ROLE_ALREADY_DELETED = "Role is already inactive";

    // System module response messages
    public static final String SYSTEM_MODULE_NAME_ALREADY_EXISTS = "Module name already exists";
    public static final String SYSTEM_MODULE_NOT_FOUND = "Module not found";
    public static final String SYSTEM_MODULE_STATUS_ALREADY_UPDATED = "Module status is already set to the requested status";

    // Permission response messages
    public static final String PERMISSION_NOT_FOUND = "Permission not found";
    public static final String PERMISSION_NAME_ALREADY_EXISTS = "Permission name already exists";
    public static final String PERMISSION_STATUS_ALREADY_UPDATED = "Permission status is already updated";
    public static final String MODULE_NOT_FOUND ="System module not found";
}