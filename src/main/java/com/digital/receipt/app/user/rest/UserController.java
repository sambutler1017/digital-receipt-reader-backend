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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * Gets a list of users based of the request filter
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
     * Get user object for the given Id
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
}
