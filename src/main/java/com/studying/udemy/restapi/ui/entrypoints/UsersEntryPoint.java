package com.studying.udemy.restapi.ui.entrypoints;

import com.studying.udemy.restapi.annotation.Secured;
import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.service.impl.UserServiceImpl;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.request.CreateUserRequestModel;
import com.studying.udemy.restapi.ui.model.response.UserProfileRest;
import org.springframework.beans.BeanUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

    @Secured
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

    @Secured
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserProfileRest> getUsers(@DefaultValue("0") @QueryParam("start") int start,
                                          @DefaultValue("50") @QueryParam("limit") int limit) {
        UserService userService = new UserServiceImpl();
        List<UserDTO> userProfileList = userService.getUsers(start, limit);

//        Prepare return value


        return userProfileList.stream().map(this::convertUserDTOInProfileRest).collect(Collectors.toList());
    }

    private UserProfileRest convertUserDTOInProfileRest(UserDTO userProfile) {
        UserProfileRest result = new UserProfileRest();
        BeanUtils.copyProperties(userProfile, result);
        result.setHref("/users/" + userProfile.getUserId());

        return result;
    }
}
