package com.studying.udemy.restapi.service;

import com.studying.udemy.restapi.shared.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO user);
    UserDTO getUser(String id);
    UserDTO getUserByUserName(String userEmail);
}
