package com.project.milkcollection.exception;

//feat(exception): add forbidden exception
//Thrown when an authenticated user does not have sufficient permissions to access a resource.
public class ForbiddenException extends BusinessException {

    public ForbiddenException(final String message) {
        super(message);
    }

    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}