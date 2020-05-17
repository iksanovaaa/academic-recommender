package com.shitajimado.academicwritingrecommender.core.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}