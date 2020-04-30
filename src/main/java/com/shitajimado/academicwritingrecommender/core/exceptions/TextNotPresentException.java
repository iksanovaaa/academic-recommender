package com.shitajimado.academicwritingrecommender.core.exceptions;

public class TextNotPresentException extends Exception {
    public TextNotPresentException(String errorMessage) {
        super(errorMessage);
    }
}