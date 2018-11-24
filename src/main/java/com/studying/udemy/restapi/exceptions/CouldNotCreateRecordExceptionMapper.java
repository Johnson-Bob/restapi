package com.studying.udemy.restapi.exceptions;

import com.studying.udemy.restapi.ui.model.response.ErrorMessage;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CouldNotCreateRecordExceptionMapper implements ExceptionMapper<CouldNotCreateRecordException> {
    @Override
    public Response toResponse(CouldNotCreateRecordException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), ErrorMessages.RECORD_ALREADY_EXISTS.name(),
                "http://localhost:8080/rest-api");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}
