package com.studying.udemy.restapi.service.impl;

import com.studying.udemy.restapi.exceptions.CouldNotCreateRecordException;
import com.studying.udemy.restapi.exceptions.CouldNotDeleteRecordException;
import com.studying.udemy.restapi.exceptions.CouldNotUpdateRecordException;
import com.studying.udemy.restapi.exceptions.NoRecordFoundException;
import com.studying.udemy.restapi.io.dao.DAO;
import com.studying.udemy.restapi.io.dao.impl.MySQLDAO;
import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.response.ErrorMessages;
import com.studying.udemy.restapi.utils.UserProfileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UserServiceImpl implements UserService {
    private DAO database;
    private UserProfileUtils profileUtils;

    public UserServiceImpl() {
        this.database = new MySQLDAO();
        this.profileUtils = new UserProfileUtils();
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        UserDTO returnValue = null;

//        Validate the required fields
        profileUtils.validateRequiredFields(user);

//        Check if user already exists
        UserDTO existingUser = getUserByUserName(user.getEmail());
        if (existingUser != null) {
            throw new CouldNotCreateRecordException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

//        Generate secure public user id
        String userId = profileUtils.generateUserId(30);
        user.setUserId(userId);

//        Generate salt
        String salt = profileUtils.generateSalt(30);

//        Generate secure password
        String encryptedPassword = profileUtils.generateSecurePassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setEncryptedPassword(encryptedPassword);

//        Record data into the database
        returnValue = saveUser(user);

//        Return back a user profile
        return returnValue;
    }

    @Override
    public UserDTO getUser(String id) {
        UserDTO returnValue = null;
        try {
            database.openConnection();
            returnValue = database.getUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoRecordFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        } finally {
            database.closeConnection();
        }

        return returnValue;
    }

    @Override
    public UserDTO getUserByUserName(String userEmail) {
        UserDTO userDTO = null;

        if (StringUtils.isBlank(userEmail)) {
            return userDTO;
        }

//        Connect to database
        try {
            database.openConnection();
            userDTO = database.getUserByUserName(userEmail);
        } finally {
            database.closeConnection();
        }

        return userDTO;
    }

    @Override
    public List<UserDTO> getUsers(int start, int limit) {
        List<UserDTO> returnValue = null;

        try {
            database.openConnection();
            returnValue = database.getUsers(start, limit);
        } finally {
            database.closeConnection();
        }

        return returnValue;
    }

    @Override
    public void updateUserDetails(UserDTO userProfile) {
        try {
            database.openConnection();
            database.updateUser(userProfile);
        } catch (Exception e) {
            throw new CouldNotUpdateRecordException(e.getMessage());
        } finally {
            database.closeConnection();
        }
    }

    @Override
    public void deleteUser(UserDTO userProfile) {
        try {
            database.openConnection();
            database.deleteUser(userProfile);
        }catch (Exception e) {
            throw new CouldNotDeleteRecordException(e.getMessage());
        }finally {
            database.closeConnection();
        }
    }

    private UserDTO saveUser(UserDTO user) {
        UserDTO returnValue = null;
        //        Connect to database
        try {
            database.openConnection();
            returnValue = database.saveUser(user);
        } finally {
            database.closeConnection();
        }

        return returnValue;
    }
}
