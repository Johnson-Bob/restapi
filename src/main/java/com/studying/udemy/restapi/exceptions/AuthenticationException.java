package com.studying.udemy.restapi.exceptions;

public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -804325624453799205L;

    public AuthenticationException(String message) {
        super(message);
    }
}
