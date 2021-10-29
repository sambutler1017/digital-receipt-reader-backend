package com.digital.receipt.app.user.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.user.client.domain.PasswordUpdate;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.service.UserService;
import com.digital.receipt.common.enums.WebRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User Controller for dealing with user request and modifications.
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@RequestMapping("api/user-app/users")
@RestApiController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Gets a list of users based of the request filter.
     * 
     * @param request to filter on
     * @return list of user objects
     * @throws Exception
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public List<User> getUsers(UserGetRequest request) throws Exception {
        return userService.getUsers(request);
    }

    /**
     * Get user object for the given Id.
     * 
     * @param id of the user
     * @return user associated to that id
     * @throws Exception
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public User getUserById(@PathVariable int id) throws Exception {
        return userService.getUserById(id);
    }

    /**
     * Gets the current logged in user information.
     * 
     * @return The user currently logged in.
     * @throws Exception
     */
    @GetMapping(path = "/current-user", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public User getCurrentUser() throws Exception {
        return userService.getCurrentUser();
    }

    /**
     * This will check to see if the email exists. If it does then it will return
     * true, otherwise false.
     * 
     * @param email The email to check
     * @return {@link Boolean} to see if the email exists
     * @throws Exception
     */
    @GetMapping("/check-email")
    public boolean doesEmailExist(@RequestBody String email) throws Exception {
        return userService.doesEmailExist(email);
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
    @PutMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public User updateUser(@RequestBody User user) throws Exception {
        return userService.updateUser(user);
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    @PutMapping(path = "/{id}/role/{role}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public User updateUserRole(@PathVariable int id, @PathVariable String role) throws Exception {
        return userService.updateUserRole(id, WebRole.valueOf(role));
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
    @PutMapping(path = "/password", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public User updatePassword(@RequestBody PasswordUpdate passUpdate) throws Exception {
        return userService.updatePassword(passUpdate);
    }

    /**
     * This gets called when a user forgets their password. This will check to see
     * if the passed in email exists as a user, if it does then the user will get an
     * email to reset their passowrd.
     * 
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    @PostMapping(path = "/forgot-password", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public User forgotPassword(@RequestBody String email) throws Exception {
        return userService.forgotPassword(email);
    }

    /**
     * Will delete a user for the given id. This endpoint can only be accessed by a
     * user with admin access.
     * 
     * @param id of the user that is to be deleted.
     * @throws Exception
     */
    @DeleteMapping("/{id}")
    @HasAccess(WebRole.ADMIN)
    public void deleteUser(@PathVariable int id) throws Exception {
        userService.deleteUser(id);
    }
}
