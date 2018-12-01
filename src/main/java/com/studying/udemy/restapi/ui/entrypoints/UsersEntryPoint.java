package com.studying.udemy.restapi.ui.entrypoints;

import com.studying.udemy.restapi.annotation.Secured;
import com.studying.udemy.restapi.service.UserService;
import com.studying.udemy.restapi.service.impl.UserServiceImpl;
import com.studying.udemy.restapi.shared.dto.UserDTO;
import com.studying.udemy.restapi.ui.model.RequestOperation;
import com.studying.udemy.restapi.ui.model.ResponseStatus;
import com.studying.udemy.restapi.ui.model.request.CreateUserRequestModel;
import com.studying.udemy.restapi.ui.model.request.UpdateUserRequestModel;
import com.studying.udemy.restapi.ui.model.response.DeleteUserProfileResponseModel;
import com.studying.udemy.restapi.ui.model.response.UserProfileRest;
import org.apache.commons.lang3.StringUtils;
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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest updateUserDetails(@PathParam("id") String id,
                                             UpdateUserRequestModel userDetails) {
        UserService userService = new UserServiceImpl();
        UserDTO userStoredProfile = userService.getUser(id);

//        Set only those fields you would like to be updated with this request
        if (StringUtils.isNotBlank(userDetails.getFirstName())) {
            userStoredProfile.setFirstName(userDetails.getFirstName());
        }

        if (StringUtils.isNotBlank(userDetails.getLastName())) {
            userStoredProfile.setLastName(userDetails.getLastName());
        }

//        Update user details
        userService.updateUserDetails(userStoredProfile);

        return convertUserDTOInProfileRest(userStoredProfile);
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeleteUserProfileResponseModel deleteUserProfile(@PathParam("id") String id) {
        DeleteUserProfileResponseModel returnValue = new DeleteUserProfileResponseModel();
        returnValue.setRequestOperation(RequestOperation.DELETE);

        UserService userService = new UserServiceImpl();
        UserDTO storedUserProfile = userService.getUser(id);

        userService.deleteUser(storedUserProfile);

        returnValue.setResponseStatus(ResponseStatus.SUCCESS);

        return returnValue;
    }

    private UserProfileRest convertUserDTOInProfileRest(UserDTO userProfile) {
        UserProfileRest result = new UserProfileRest();
        BeanUtils.copyProperties(userProfile, result);
        result.setHref("/users/" + userProfile.getUserId());

        return result;
    }
}
