package com.studying.udemy.restapi.io.dao;

import com.studying.udemy.restapi.shared.dto.UserDTO;

import java.util.List;

public interface DAO {
    void openConnection();
    UserDTO getUserByUserName(String userName);
    UserDTO saveUser(UserDTO user);
    void updateUser(UserDTO user);
    UserDTO getUser(String id);
    List<UserDTO> getUsers(int start, int limit);
    void closeConnection();
}
