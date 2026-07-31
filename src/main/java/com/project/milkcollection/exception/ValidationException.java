package com.project.milkcollection.exception;

//feat(exception): add validation exception
//Thrown when business validation rules are violated
public class ValidationException extends BusinessException{

    public ValidationException(final String message){
        super(message);
    }

    public ValidationException(final String message, final Throwable cause){
        super(message, cause);
    }
}
