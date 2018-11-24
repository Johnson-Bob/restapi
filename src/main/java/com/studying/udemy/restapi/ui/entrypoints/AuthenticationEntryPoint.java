package com.studying.udemy.restapi.ui.entrypoints;

import com.studying.udemy.restapi.service.AuthenticationService;
import com.studying.udemy.restapi.service.impl.AuthenticationServiceImpl;
import com.studying.udemy.restapi.shared.dto.UserDTO;
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

        AuthenticationService authService = new AuthenticationServiceImpl();
        UserDTO authUser = authService.authenticate(loginCredentials.getUserName(),
                loginCredentials.getUserPassword());

//        Reset access token
        authService.resetSecurityCredentials(loginCredentials.getUserPassword(), authUser);

        String authToken = authService.issueAuthenticationToken(authUser);

        returnValue.setUserId(authUser.getUserId());
        returnValue.setToken(authToken);

        return returnValue;
    }
}
