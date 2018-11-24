package com.studying.udemy.restapi.exceptions;

import javax.ws.rs.ext.Provider;

@Provider
public class NoRecordFoundException extends RuntimeException {
    private static final long serialVersionUID = -1774169830161174989L;

    public NoRecordFoundException(String message) {
        super(message);
    }
}
