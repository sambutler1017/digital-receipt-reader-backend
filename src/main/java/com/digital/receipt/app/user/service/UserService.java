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
import com.digital.receipt.service.util.PasswordHash;
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
        updateUserPassword(user.getPassword());
        updateUserForgotPasswordFlag(jwtHolder.getRequiredUserId(), user.isForgotPassword());
        return updateUserProfile(user);
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserRole(int id, WebRole role) throws Exception {
        return userDao.updateUserRole(id, role);
    }

    /**
     * This gets called when a user forgets their password. They will set the forgot
     * password flag and get an email with the temporary password.
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
        return updateUserForgotPasswordFlag(users.get(0).getId(), true);
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
     * Update the user for the given user object.
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    private User updateUserProfile(User user) throws Exception {
        return userDao.updateUserProfile(user);
    }

    /**
     * Update the users credentials.
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    private User updateUserPassword(String password) throws Exception {
        try {
            if (password != null && password.trim() != "") {
                return userDao.updateUserPassword(PasswordHash.hashPassword(password));
            } else {
                return getCurrentUser();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException("Could not hash password!");
        }
    }

    /**
     * Will set the forgot password flag to the given boolean value for the given
     * userId.
     * 
     * @param userId The id of the user to update.
     * @param flag   The flag to set the forgot password too.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    private User updateUserForgotPasswordFlag(int userId, boolean flag) throws Exception {
        return userDao.updateUserForgotPasswordFlag(userId, flag);
    }
}
