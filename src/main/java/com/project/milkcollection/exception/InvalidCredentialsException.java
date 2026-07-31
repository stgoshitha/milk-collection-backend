package com.project.milkcollection.exception;

//Thrown when a user provides invalid authentication credentials
public class InvalidCredentialsException extends UnauthorizedException{

    public InvalidCredentialsException(final String message){
        super(message);
    }

    public InvalidCredentialsException(final String message, final Throwable cause){
        super(message, cause);
    }
}
