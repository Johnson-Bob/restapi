package com.studying.udemy.restapi.service.impl;

import com.studying.udemy.restapi.exceptions.AuthenticationException;
import com.studying.udemy.restapi.io.dao.DAO;
import com.studying.udemy.restapi.io.dao.impl.MySQLDAO;
import com.studying.udemy.restapi.service.AuthenticationService;
import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;
import com.studying.udemy.restapi.utils.UserProfileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationServiceImpl implements AuthenticationService {
    private DAO database;

    @Override
    public UserDTO authenticate(String userName, String userPassword) throws AuthenticationException {
        UserService userService = new UserServiceImpl();
        UserDTO storedUser = userService.getUserByUserName(userName);

        if (storedUser == null) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        }

        String encryptedPassword = new UserProfileUtils().generateSecurePassword(userPassword, storedUser.getSalt());
        boolean authenticated = false;
        if (StringUtils.equalsIgnoreCase(storedUser.getEncryptedPassword(), encryptedPassword)) {
            authenticated = true;
        }

        if (!authenticated) {
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        }

        return storedUser;
    }

    @Override
    public String issueAuthenticationToken(UserDTO userProfile) throws AuthenticationException {
        String returnValue = null;

        String accessTokenMaterial = userProfile.getUserId() + userProfile.getSalt();

        byte[] encryptedAccessToken = null;
        try {
            encryptedAccessToken = new UserProfileUtils().encrypt(userProfile.getEncryptedPassword(), accessTokenMaterial);
        }catch (AssertionError e) {
            throw new AuthenticationException("Failed to issue secure access token");
        }

//        Split token into equals part
        String tokenEncoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
        int tokenLength = tokenEncoded.length();
        String tokenToSaveToDatabase = StringUtils.substring(tokenEncoded, 0, tokenLength/2);
        returnValue = StringUtils.substring(tokenEncoded,  tokenLength/2, tokenLength);

        userProfile.setToken(tokenToSaveToDatabase);
        updateUserProfile(userProfile);

        return returnValue;
    }

    @Override
    public void resetSecurityCredentials(String userPassword, UserDTO userDTO) {
//        Generate a new salt
        UserProfileUtils utils = new UserProfileUtils();
        String salt = utils.generateSalt(30);

//        Generate a new password
        String securePassword = utils.generateSecurePassword(userPassword, salt);

//        Update a user profile
        userDTO.setSalt(salt);
        userDTO.setEncryptedPassword(securePassword);
        updateUserProfile(userDTO);
    }

    private void updateUserProfile(UserDTO userProfile) {
        if (this.database == null) {
            this.database = new MySQLDAO();
        }

        try {
            database.openConnection();
            database.updateUser(userProfile);
        } finally {
            database.closeConnection();
        }
    }
}
