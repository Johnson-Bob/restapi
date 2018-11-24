package com.studying.udemy.restapi.service;

import com.studying.udemy.restapi.exceptions.AuthenticationException;
import com.studying.udemy.restapi.shared.dto.UserDTO;

public interface AuthenticationService {
    UserDTO authenticate(String userName, String userPassword) throws AuthenticationException;
    String issueAuthenticationToken(UserDTO userDTO) throws AuthenticationException;
    void resetSecurityCredentials(String userPassword, UserDTO userDTO);
}
