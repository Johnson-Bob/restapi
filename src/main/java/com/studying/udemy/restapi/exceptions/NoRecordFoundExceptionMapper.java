package com.studying.udemy.restapi.exceptions;

import com.studying.udemy.restapi.ui.model.response.ErrorMessage;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoRecordFoundExceptionMapper implements ExceptionMapper<NoRecordFoundException> {
    @Override
    public Response toResponse(NoRecordFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), ErrorMessages.NO_RECORD_FOUND.name(),
                "http://localhost:8080");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}

