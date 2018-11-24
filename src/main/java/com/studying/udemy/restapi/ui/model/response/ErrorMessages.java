package com.studying.udemy.restapi.ui.model.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required field"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided id is not found");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
