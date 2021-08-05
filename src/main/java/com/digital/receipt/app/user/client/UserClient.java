package com.digital.receipt.app.user.client;

import java.io.IOException;
import java.util.List;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.rest.UserController;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class exposes the user endpoint's to other app's to pull data across the
 * platform.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
public class UserClient {

    @Autowired
    private UserController userController;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public List<User> getUsers(UserGetRequest request) throws SqlFragmentNotFoundException, IOException {
        return userController.getUsers(request);
    }

    /**
     * Client method to get the user given a user id
     * 
     * @param id of the user
     * @return User profile object
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     * @throws Exception
     */
    public User getUserById(int id) throws SqlFragmentNotFoundException, IOException {
        return userController.getUserById(id);
    }
}
