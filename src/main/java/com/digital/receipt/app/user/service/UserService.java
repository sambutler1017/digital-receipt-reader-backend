package com.digital.receipt.app.user.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.digital.receipt.app.auth.client.AuthenticationClient;
import com.digital.receipt.app.auth.client.domain.DigitalReceiptToken;
import com.digital.receipt.app.email.client.EmailClient;
import com.digital.receipt.app.receipt.client.ReceiptClient;
import com.digital.receipt.app.user.client.domain.PasswordUpdate;
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

    @Autowired
    private AuthenticationClient authClient;

    @Autowired
    private ReceiptClient receiptClient;

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
     * This will create a new user based on the given {@link User} object.
     * 
     * @param user The user to be created.
     * @throws Exception If the user can not be created.
     */
    public User createUser(User user) throws Exception {
        User newUser = userDao.createUser(user);
        createUserPassword(newUser.getId(), user.getPassword());
        return getUserById(newUser.getId());
    }

    /**
     * Update the user profile for the given user object.
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUser(User user) throws Exception {
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
     * This will insert the given auth password into the database for the given user
     * id.
     * 
     * @param id           The id of the created user to attach it too.
     * @param authPassword The password and salt value to insert.
     * @throws Exception If the sql process can not be completed.
     */
    public void createUserPassword(int userId, String userPassword) throws Exception {
        userDao.createUserPassword(userId, PasswordUtil.hashPasswordWithSalt(userPassword));
    }

    /**
     * This will take in a {@link PasswordUpdate} object that will confirm that the
     * current password matches the database password. If it does then it will
     * update the password to the new password.
     * 
     * @param passUpdate Object the holds the current password and new user password
     *                   to change it too.
     * @return {@link User} object of the user that was updated.
     * @throws Exception If the user can not be authenticated or the function was
     *                   not able to hash the new password.
     */
    public User updatePassword(PasswordUpdate passUpdate) throws Exception {
        DigitalReceiptToken token = authClient
                .authenticateUser(jwtHolder.getRequiredEmail(), passUpdate.getCurrentPassword()).getBody();

        try {
            if (passUpdate.getNewPassword() != null && passUpdate.getNewPassword().trim() != "") {
                return userDao.updateUserPassword(token.getUser().getId(),
                        PasswordUtil.hashPasswordWithSalt(passUpdate.getNewPassword()));
            } else {
                return getCurrentUser();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException("Could not hash password!");
        }
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
        receiptClient.deleteAllUserReceipts(id);
        userDao.deleteUser(id);
    }

    /**
     * This will allow a user to delete their account. This will delete all their
     * receipts, personal information, and login credentials.
     * 
     * @throws Exception
     */
    public void currentUserDeleteAccount() throws Exception {
        deleteUser(jwtHolder.getRequiredUserId());
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
}
