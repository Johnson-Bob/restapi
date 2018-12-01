package com.studying.udemy.restapi.exceptions;

public class CouldNotUpdateRecordException extends RuntimeException {
    private static final long serialVersionUID = 2386520931926231714L;

    public CouldNotUpdateRecordException(String message) {
        super(message);
    }
}
