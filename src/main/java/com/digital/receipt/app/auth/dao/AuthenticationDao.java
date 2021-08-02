package com.digital.receipt.app.auth.dao;

import static com.digital.receipt.app.auth.mapper.AuthenticationMapper.AUTH_MAPPER;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.exceptions.BaseException;
import com.digital.receipt.service.sql.SqlBuilder;
import com.digital.receipt.service.sql.SqlClient;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

/**
 * Class that handles all the dao calls to the database for users
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Component
public class AuthenticationDao {

    @Autowired
    private SqlClient sqlClient;

    @Autowired
    private SqlBuilder sqlBuilder;

    /**
     * Not an exposed endpoint, strictly used by the authentication controller to
     * autheticate a user.
     * 
     * @param username To search for in the database
     * @param password The password to validate against
     * @return User object if the user credentials are correct.
     * @throws BaseException
     */
    public User authenticateUser(String username, String password) throws BaseException {
        Map<String, Set<?>> params = new HashMap<>();
        params.put("username", Sets.newHashSet(username));
        params.put("password", Sets.newHashSet(password));

        sqlBuilder.setQueryFile("authDAO");
        sqlBuilder.setParams(params);

        try {
            return sqlClient.getTemplate(sqlBuilder.getSql("authenticateUser"), AUTH_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
