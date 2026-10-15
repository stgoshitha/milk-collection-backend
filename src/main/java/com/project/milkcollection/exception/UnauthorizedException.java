package com.project.milkcollection.exception;

//feat(exception): add unauthorized exception
//Thrown when a user is not authenticated or provides invalid authentication credentials
public class UnauthorizedException extends BusinessException{
    public UnauthorizedException(final String message) {
        super(message);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
