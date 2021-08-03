package com.digital.receipt.app.user.service;

import java.util.List;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User Service class that handles all service calls to the dao.
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     */
    public List<User> getUsers(UserGetRequest request) {
        return userDao.getUsers(request);
    }

    /**
     * Service to get a users profile given the user id
     * 
     * @param id of the user
     * @return User profile object {@link User}
     */
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }
}
