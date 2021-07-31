package com.digital.receipt.app.user.rest;

import java.util.List;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/user-app/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Gets a list of users based of the request filter
     * 
     * @param request to filter on
     * @return list of user objects
     */
    @GetMapping()
    public List<User> getUsers(UserGetRequest request) {
        return userService.getUsers(request);
    }

    /**
     * Get user object for the given Id
     * 
     * @param id of the user
     * @return user associated to that id
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
