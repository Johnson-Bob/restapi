package com.studying.udemy.restapi.exceptions;

public class CouldNotCreateRecordException extends RuntimeException {

    private static final long serialVersionUID = -1710976136981321834L;

    public CouldNotCreateRecordException(String message) {
        super(message);
    }
}
