package com.digital.receipt.app.user.service;

import java.io.IOException;
import java.util.List;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.dao.UserDao;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.jwt.model.AuthenticationRequest;

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
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public List<User> getUsers(UserGetRequest request) throws SqlFragmentNotFoundException, IOException {
        return userDao.getUsers(request);
    }

    /**
     * Service to get a users profile given the user id
     * 
     * @param id of the user
     * @return User profile object {@link User}
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User getUserById(int id) throws SqlFragmentNotFoundException, IOException {
        return userDao.getUserById(id);
    }

    /**
     * Update the user for the given user object
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserProfile(User user) throws SqlFragmentNotFoundException, IOException {
        return userDao.updateUserProfile(user);
    }

    /**
     * Update the users credentials
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserPassword(AuthenticationRequest authRequest) throws SqlFragmentNotFoundException, IOException {
        return userDao.updateUserPassword(authRequest);
    }
}
