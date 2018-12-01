package com.studying.udemy.restapi.filters;

import com.studying.udemy.restapi.annotation.Secured;
import com.studying.udemy.restapi.exceptions.AuthenticationException;
import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.service.impl.UserServiceImpl;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.utils.UserProfileUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
//        Extract authorisation header details
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            throw new AuthenticationException("Authorization header must be provided");
        }

//        Extract UserId
        String userId = requestContext.getHeaderString("User-Id");
        if (userId == null) {
            throw new AuthenticationException("User-Id header must be provided");
        }

        validateToken(token, userId);
    }

    private void validateToken(String token, String userId) throws AuthenticationException {
//        Get user profile details
        UserService userService = new UserServiceImpl();
        UserDTO userProfile = userService.getUser(userId);

//        Assemble Access token using two parts. One from DB, one from Http request
        String completeToken = userProfile.getToken() + token;

//        Create Access token material out of the userId received and salt kept in database
        String securePassword = userProfile.getEncryptedPassword();
        String salt = userProfile.getSalt();
        String accessTokenMaterial = userId + salt;
        byte[] encryptedAccessToken = null;

        try {
            encryptedAccessToken = new UserProfileUtils().encrypt(securePassword, accessTokenMaterial);
        } catch (AssertionError e) {
            Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, e);
            throw new AuthenticationException("Failed to issue secure access token");
        }

        String encryptedTokenEncoded = Base64.getEncoder().encodeToString(encryptedAccessToken);

//        Compare two access token
        if (!Objects.equals(completeToken, encryptedTokenEncoded)) {
            throw new AuthenticationException("Authorization token did not match");
        }
    }
}
