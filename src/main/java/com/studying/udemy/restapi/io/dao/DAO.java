package com.studying.udemy.restapi.io.dao;

import com.studying.udemy.restapi.shared.dto.UserDTO;

public interface DAO {
    void openConnection();
    UserDTO getUserByUserName(String userName);
    UserDTO saveUser(UserDTO user);
    void updateUser(UserDTO user);
    UserDTO getUser(String id);
    void closeConnection();
}
