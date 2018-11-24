package com.studying.udemy.restapi.ui.entrypoints;

import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.service.impl.UserServiceImpl;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.request.CreateUserRequestModel;
import com.studying.udemy.restapi.ui.model.response.UserProfileRest;
import org.springframework.beans.BeanUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UsersEntryPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest createUser(CreateUserRequestModel requestObject) {
        UserProfileRest returnValue = new UserProfileRest();

//        Prepare UserDTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestObject, userDTO);

//        Create new User
        UserService userService = new UserServiceImpl();
        UserDTO createdUserProfile = userService.createUser(userDTO);

//        Prepare response
        BeanUtils.copyProperties(createdUserProfile, returnValue);
        return returnValue;
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest getUserProfile(@PathParam("id")String id) {
        UserProfileRest returnValue = null;

        UserService userService = new UserServiceImpl();
        UserDTO userProfile = userService.getUser(id);

        returnValue = new UserProfileRest();
        BeanUtils.copyProperties(userProfile, returnValue);

        return returnValue;
    }
}
