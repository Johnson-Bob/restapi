package com.studying.udemy.restapi.service;

import com.studying.udemy.restapi.shared.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO user);
    UserDTO getUser(String id);
    UserDTO getUserByUserName(String userEmail);
    List<UserDTO> getUsers(int staert, int limit);
    void updateUserDetails(UserDTO userProfile);
}
