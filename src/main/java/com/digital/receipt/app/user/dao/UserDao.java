package com.digital.receipt.app.user.dao;

import static com.digital.receipt.app.user.mapper.UserMapper.USER_MAPPER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.service.sql.SqlBuilder;
import com.digital.receipt.service.sql.SqlClient;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class that handles all the dao calls to the database for users
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Repository
public class UserDao {

    @Autowired
    private SqlClient sqlClient;

    @Autowired
    private SqlBuilder sqlBuilder;

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     */
    public List<User> getUsers(UserGetRequest request) {
        Map<String, Set<?>> params = new HashMap<>();
        if (request.getId() != null)
            params.put("id", request.getId());
        if (request.getEmail() != null)
            params.put("email", request.getEmail());

        sqlBuilder.setQueryFile("userDAO");
        sqlBuilder.setParams(params);

        return sqlClient.getPage(sqlBuilder.getSql("getUsers"), USER_MAPPER);
    }

    /**
     * This method returns a user profile object containing profile type information
     * about the user
     * 
     * @param id of the user
     * @return User profile object {@link UserProfile}
     */
    public User getUserById(int id) {
        Map<String, Set<?>> params = new HashMap<>();
        params.put("userId", Sets.newHashSet(id));

        sqlBuilder.setQueryFile("userDAO");
        sqlBuilder.setParams(params);

        return sqlClient.getTemplate(sqlBuilder.getSql("getUserById"), USER_MAPPER);
    }
}
