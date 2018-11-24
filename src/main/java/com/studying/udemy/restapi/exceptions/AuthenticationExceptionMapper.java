package com.studying.udemy.restapi.exceptions;

import com.studying.udemy.restapi.ui.model.response.ErrorMessage;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), ErrorMessages.AUTHENTICATION_FAILED.name(),
                "http://localhost:8080/rest-api");
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
    }
}
