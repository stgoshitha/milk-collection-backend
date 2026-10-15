package com.project.milkcollection.exception;

//"feat(exception): add base business exception"

//Base exception for all business-related exceptions.
public class BusinessException extends RuntimeException{

    public BusinessException(final String message){
        super(message);
    }

    public BusinessException(final String message, final Throwable cause){
        super(message,cause);
    }

}
