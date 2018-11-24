package com.studying.udemy.restapi.exceptions;

import com.studying.udemy.restapi.ui.model.response.ErrorMessage;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MissingRequiredFieldExceptionMapper implements ExceptionMapper<MissingRequiredFieldException> {
    @Override
    public Response toResponse(MissingRequiredFieldException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), ErrorMessages.MISSING_REQUIRED_FIELD.name(),
                "http://localhost:8080");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}
