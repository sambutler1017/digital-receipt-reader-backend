package com.digital.receipt.app.user.client;

import java.util.List;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.rest.UserController;

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
    private UserController controller;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     * @throws Exception
     */
    public List<User> getUsers(UserGetRequest request) throws Exception {
        return controller.getUsers(request);
    }

    /**
     * Client method to get the user given a user id
     * 
     * @param id of the user
     * @return User profile object
     * @throws Exception
     */
    public User getUserById(int id) throws Exception {
        return controller.getUserById(id);
    }

    /**
     * Gets the current logged in user information.
     * 
     * @return The user currently logged in.
     * @throws Exception
     */
    public User getCurrentUser() throws Exception {
        return controller.getCurrentUser();
    }

    /**
     * Update the user's information such as email, first name, last name, and
     * password
     * 
     * @param id   of the user
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUser(User user) throws Exception {
        return controller.updateUser(user);
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserRole(int id, String role) throws Exception {
        return controller.updateUserRole(id, role);
    }

    /**
     * This gets called when a user forgets their password. They will set the forgot
     * password flag and get an email with the temporary password.
     * 
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User forgotPassword(String email) throws Exception {
        return controller.forgotPassword(email);
    }

    /**
     * Will delete a user for the given id. This endpoint can only be accessed by a
     * user with admin access.
     * 
     * @param id of the user that is to be deleted.
     * @throws Exception
     */
    public void deleteUser(int id) throws Exception {
        controller.deleteUser(id);
    }
}
