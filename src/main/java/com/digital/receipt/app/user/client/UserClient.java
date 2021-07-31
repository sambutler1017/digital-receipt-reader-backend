package com.digital.receipt.app.user.client;

import java.util.List;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.rest.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class exposes the user endpoint's to other app's to pull data across the
 * platform.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Component
public class UserClient {

    @Autowired
    private UserController userController;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     */
    public List<User> getUsers(UserGetRequest request) {
        return userController.getUsers(request);
    }

    /**
     * Client method to get the user given a user id
     * 
     * @param id of the user
     * @return User profile object
     * @throws Exception
     */
    public User getUserById(int id) throws Exception {
        return userController.getUserById(id);
    }
}
