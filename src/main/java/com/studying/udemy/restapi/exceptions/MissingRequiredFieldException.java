package com.studying.udemy.restapi.exceptions;

public class MissingRequiredFieldException extends RuntimeException {

    private static final long serialVersionUID = -8451775250825649450L;

    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
