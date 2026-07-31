package com.project.milkcollection.exception;

//feat(exception): add conflict exception
//Thrown when a request conflicts with the current state of the application.
public class ConflictException extends BusinessException {

    public ConflictException(final String message) {
        super(message);
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }
}