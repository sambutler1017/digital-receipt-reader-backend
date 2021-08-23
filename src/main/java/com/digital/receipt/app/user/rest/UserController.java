package com.digital.receipt.app.user.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.service.UserService;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public List<User> getUsers(UserGetRequest request) throws SqlFragmentNotFoundException, IOException {
        return userService.getUsers(request);
    }

    /**
     * Get user object for the given Id.
     * 
     * @param id of the user
     * @return user associated to that id
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public User getUserById(@PathVariable int id) throws SqlFragmentNotFoundException, IOException {
        return userService.getUserById(id);
    }

    /**
     * Gets the current logged in user information.
     * 
     * @return The user currently logged in.
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    @GetMapping(path = "/current-user", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public User getCurrentUser() throws SqlFragmentNotFoundException, IOException {
        return userService.getCurrentUser();
    }

    /**
     * Update the user's information such as email, first name, last name, and
     * password
     * 
     * @param id   of the user
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    @PutMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public User updateUser(@RequestBody User user) throws SqlFragmentNotFoundException, IOException {
        return userService.updateUser(user);
    }

    /**
     * Will delete a user for the given id. This endpoint can only be accessed by a
     * user with admin access.
     * 
     * @param id of the user that is to be deleted.
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    @DeleteMapping("/{id}")
    @HasAccess(WebRole.ADMIN)
    public void deleteUser(@PathVariable int id) throws SqlFragmentNotFoundException, IOException {
        userService.deleteUser(id);
    }
}
