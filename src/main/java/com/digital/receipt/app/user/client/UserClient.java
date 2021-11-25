package com.digital.receipt.app.user.client;

import java.util.List;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.user.client.domain.PasswordUpdate;
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
     * This will check to see if the email exists. If it does then it will return
     * true, otherwise false.
     * 
     * @param email The email to check
     * @return {@link Boolean} to see if the email exists
     * @throws Exception
     */
    public boolean doesEmailExist(String email) throws Exception {
        return controller.doesEmailExist(email);
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
     * This will take in a {@link PasswordUpdate} object that will confirm that the
     * current password matches the database password. If it does then it will
     * update the password to the new password.
     * 
     * @param passUpdate Object the holds the current password and new user password
     *                   to change it too.
     * @return {@link User} object of the user that was updated.
     * @throws Exception If the user can not be authenticated or it failed to hash
     *                   the new password.
     */
    public User updateUserPassword(PasswordUpdate passUpdate) throws Exception {
        return controller.updateUserPassword(passUpdate);
    }

    /**
     * This gets called when a user forgets their password. This will check to see
     * if the passed in email exists as a user, if it does then the user will get an
     * email to reset their passowrd.
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
