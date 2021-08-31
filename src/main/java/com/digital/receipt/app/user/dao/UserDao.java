package com.digital.receipt.app.user.dao;

import static com.digital.receipt.app.user.mapper.UserMapper.USER_MAPPER;

import java.util.List;

import com.digital.receipt.app.auth.client.domain.AuthPassword;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.common.abstracts.AbstractSqlDao;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.UserNotFoundException;

import org.springframework.stereotype.Repository;

/**
 * Class that handles all the dao calls to the database for users
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Repository
public class UserDao extends AbstractSqlDao {

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     * @throws Exception
     */
    public List<User> getUsers(UserGetRequest request) throws Exception {
        return sqlClient.getPage(getSql("getUsers"),
                params("id", request.getId()).addValue("email", request.getEmail())
                        .addValue("firstName", request.getFirstName()).addValue("lastName", request.getLastName()),
                USER_MAPPER);
    }

    /**
     * This method returns a user profile object containing profile type information
     * about the user
     * 
     * @param id of the user
     * @return User profile object {@link UserProfile}
     * @throws Exception
     */
    public User getUserById(int id) throws Exception {
        try {
            return sqlClient.getTemplate(getSql("getUserById"), params("userId", id), USER_MAPPER);
        } catch (Exception e) {
            throw new UserNotFoundException(String.format("User not found for id: %d", id));
        }
    }

    /**
     * Update the user for the given user object. Null out password field so that it
     * is not returned on the {@link User} object
     * 
     * @param userId Id of the usre being updated.
     * @param user   what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserProfile(int userId, User user) throws Exception {
        User userProfile = getUserById(userId);

        user.setPassword(null);
        user = mapNonNullUserFields(user, userProfile);

        sqlClient.update(getSql("updateUserProfile"),
                params("firstName", user.getFirstName()).addValue("lastName", user.getLastName())
                        .addValue("email", user.getEmail()).addValue("id", userProfile.getId()));

        return user;
    }

    /**
     * Update the users password, for the given password.
     * 
     * @param userId   Id of the use that is being updated.
     * @param password The password to set on the user profile.
     * @param salt     The salt value that was appended to the password.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserPassword(int userId, AuthPassword authPassword) throws Exception {
        User userProfile = getUserById(userId);

        sqlClient.update(getSql("updateUserPassword"), params("password", authPassword.getPassword())
                .addValue("id", userProfile.getId()).addValue("salt", authPassword.getSalt()));
        return userProfile;
    }

    /**
     * Will set the forgot password flag to the given boolean value.
     * 
     * @param flag The flag to set the forgot password too.
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserForgotPasswordFlag(int id, boolean flag) throws Exception {
        User userProfile = getUserById(id);
        userProfile.setForgotPassword(flag);

        sqlClient.update(getSql("updateUserForgotPassword"),
                params("flag", flag ? 1 : 0).addValue("id", userProfile.getId()));
        return userProfile;
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     * @throws Exception
     */
    public User updateUserRole(int userId, WebRole role) throws Exception {
        User userProfile = getUserById(userId);

        userProfile.setWebRole(role);
        sqlClient.update(getSql("updateUserRole"), params("id", userId).addValue("roleId", role.getValue()));
        return userProfile;
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
        sqlClient.delete(getSql("deleteUser"), params("id", id));
    }

    /**
     * Maps non null user fields from the source to the desitnation.
     * 
     * @param destination Where the null fields should be replaced.
     * @param source      Where to get the replacements for the null fields.
     * @return {@link User} with the replaced fields.
     */
    private User mapNonNullUserFields(User destination, User source) {
        if (destination.getId() == 0)
            destination.setId(source.getId());
        if (destination.getFirstName() == null)
            destination.setFirstName(source.getFirstName());
        if (destination.getLastName() == null)
            destination.setLastName(source.getLastName());
        if (destination.getEmail() == null)
            destination.setEmail(source.getEmail());
        if (destination.getWebRole() == null)
            destination.setWebRole(source.getWebRole());
        if (destination.getInsertDate() == null)
            destination.setInsertDate(source.getInsertDate());
        return destination;
    }
}
