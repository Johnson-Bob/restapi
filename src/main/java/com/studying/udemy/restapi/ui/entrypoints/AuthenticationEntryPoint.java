package com.studying.udemy.restapi.ui.entrypoints;

import com.studying.udemy.restapi.ui.model.request.LoginCredentials;
import com.studying.udemy.restapi.ui.model.response.AuthenticationDetails;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/authentication")
public class AuthenticationEntryPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AuthenticationDetails userLogin(LoginCredentials loginCredentials) {
        AuthenticationDetails returnValue = new AuthenticationDetails();

        return returnValue;
    }
}
