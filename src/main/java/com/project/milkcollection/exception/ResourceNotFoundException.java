package com.project.milkcollection.exception;

//feat(exception): add resource not found exception
//Thrown when a requested resource cannot be found
public class ResourceNotFoundException extends BusinessException{

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
