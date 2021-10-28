package com.digital.receipt.app.user.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.digital.receipt.app.email.client.EmailClient;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.app.user.dao.UserDao;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.BaseException;
import com.digital.receipt.jwt.utility.JwtHolder;
import com.digital.receipt.service.util.PasswordUtil;
import com.google.common.collect.Sets;

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

    @Autowired
    private JwtHolder jwtHolder;

    @Autowired
    private EmailClient emailClient;

    /**
     * Get users based on given request filter.
     * 
     * @param request of the user
     * @return User profile object {@link User}
     * @throws Exception
     */
    public List<User> getUsers(UserGetRequest request) throws Exception {
        return userDao.getUsers(request);
    }

    /**
     * Service to get a users profile given the user id.
     * 
     * @param id of the user
     * @return User profile object {@link User}
     * @throws Exception
     */
    public User getUserById(int id) throws Exception {
        return userDao.getUserById(id);
    }

    /**
     * Get the current user from the jwt token.
     * 
     * @return User profile object {@link User}
     * @throws Exception
     */
    public User getCurrentUser() throws Exception {
        return getUserById(jwtHolder.getRequiredUserId());
    }

    /**
     * Update the user profile for the given user object.
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUser(User user) throws Exception {
        updateUserPassword(jwtHolder.getRequiredUserId(), user.getPassword());
        return updateUserProfile(jwtHolder.getRequiredUserId(), user);
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserRole(int userId, WebRole role) throws Exception {
        return userDao.updateUserRole(userId, role);
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
        UserGetRequest request = new UserGetRequest();
        request.setEmail(Sets.newHashSet(email));
        List<User> users = getUsers(request);

        if (users.size() == 0) {
            throw new BaseException(String.format("User not found for email '%s'", email));
        }

        emailClient.forgotPassword(email);
        return users.get(0);
    }

    /**
     * Will delete a user for the given id. This endpoint can only be accessed by a
     * user with admin access.
     * 
     * @param id of the user that is to be deleted.
     * @throws Exception
     */
    public void deleteUser(int id) throws Exception {
        getUserById(id);
        userDao.deleteUser(id);
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
        UserGetRequest request = new UserGetRequest();
        request.setEmail(Sets.newHashSet(email));
        List<User> users = getUsers(request);

        return users.size() > 0;
    }

    /**
     * Update the user for the given user object.
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    private User updateUserProfile(int userId, User user) throws Exception {
        return userDao.updateUserProfile(userId, user);
    }

    /**
     * Update the users credentials.
     * 
     * @param userId   Id of the user wanting to update their password.
     * @param password THe password to update on the user's account.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    private User updateUserPassword(int userId, String password) throws Exception {
        try {
            if (password != null && password.trim() != "") {
                return userDao.updateUserPassword(userId, PasswordUtil.hashPasswordWithSalt(password));
            } else {
                return getCurrentUser();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException("Could not hash password!");
        }
    }
}
