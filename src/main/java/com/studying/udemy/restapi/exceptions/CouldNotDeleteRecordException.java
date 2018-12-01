package com.studying.udemy.restapi.exceptions;

public class CouldNotDeleteRecordException extends RuntimeException {
    private static final long serialVersionUID = -7045188345582735725L;

    public CouldNotDeleteRecordException(String message) {
        super(message);
    }
}
