package com.digital.receipt.app.user.dao;

import static com.digital.receipt.app.user.mapper.UserMapper.USER_MAPPER;

import java.io.IOException;
import java.util.List;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.UserCredentials;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
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
     * Update the user for the given user object
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserById(User user) throws SqlFragmentNotFoundException, IOException {
        return null;
    }

    /**
     * Update the users credentials
     * 
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     * @throws IOException
     * @throws SqlFragmentNotFoundException
     */
    public User updateUserCredentials(UserCredentials user) throws SqlFragmentNotFoundException, IOException {
        return null;
    }
}
