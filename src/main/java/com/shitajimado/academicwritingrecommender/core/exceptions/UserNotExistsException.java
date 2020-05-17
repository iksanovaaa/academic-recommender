package com.shitajimado.academicwritingrecommender.core.exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}