package com.digital.receipt.app.user.dao;

import static com.digital.receipt.app.user.mapper.UserMapper.USER_MAPPER;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.common.exceptions.UserNotFoundException;
import com.digital.receipt.jwt.utility.JwtHolder;
import com.digital.receipt.sql.AbstractSqlDao;
import com.digital.receipt.sql.SqlBundler;
import com.digital.receipt.sql.SqlClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class that handles all the dao calls to the database for users
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Repository
public class UserDao extends AbstractSqlDao {

    @Autowired
    private SqlClient sqlClient;

    @Autowired
    private SqlBundler bundler;

    @Autowired
    private JwtHolder jwtHolder;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public List<User> getUsers(UserGetRequest request) throws SqlFragmentNotFoundException, IOException {
        return sqlClient.getPage(
                bundler.bundle(getSql("getUsers"), params("id", request.getId()).addValue("email", request.getEmail())
                        .addValue("firstName", request.getFirstName()).addValue("lastName", request.getLastName())),
                USER_MAPPER);
    }

    /**
     * This method returns a user profile object containing profile type information
     * about the user
     * 
     * @param id of the user
     * @return User profile object {@link UserProfile}
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User getUserById(int id) throws SqlFragmentNotFoundException, IOException {
        return sqlClient.getTemplate(bundler.bundle(getSql("getUserById"), params("userId", id)), USER_MAPPER);
    }

    /**
     * Update the user for the given user object. Null out password field so that it
     * is not returned on the {@link User} object
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserProfile(User user) throws SqlFragmentNotFoundException, IOException {
        User userProfile = getUserById(jwtHolder.getRequiredUserId());
        int userId = userProfile.getId();

        user.setPassword(null);
        user = mapNonNullUserFields(user, userProfile);

        Optional<Integer> updatedRow = sqlClient.update(bundler.bundle(getSql("updateUserProfile"),
                params("firstName", user.getFirstName()).addValue("lastName", user.getLastName())
                        .addValue("email", user.getEmail()).addValue("id", userId)));

        if (!updatedRow.isPresent()) {
            throw new UserNotFoundException(
                    String.format("User not found! Could not update user for id: '%i'", userId));
        }
        return user;
    }

    /**
     * Update the users password
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserPassword(String password) throws SqlFragmentNotFoundException, IOException {
        User userProfile = getUserById(jwtHolder.getRequiredUserId());
        int userId = userProfile.getId();
        Optional<Integer> updatedRow = Optional.of(0);

        updatedRow = sqlClient.update(
                bundler.bundle(getSql("updateUserPassword"), params("password", password).addValue("id", userId)));

        if (!updatedRow.isPresent()) {
            throw new UserNotFoundException(
                    String.format("User not found! Could not update user for id: '%i'", userId));
        }
        return userProfile;
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
