package com.project.milkcollection.exception;

//Thrown when a user account is inactive or suspended
public class UserInactiveException extends UnauthorizedException {

    public UserInactiveException(final String message) {
        super(message);
    }

    public UserInactiveException(final String message, final Throwable cause) {
        super(message, cause);
    }
}