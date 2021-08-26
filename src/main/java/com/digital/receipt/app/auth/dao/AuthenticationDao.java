package com.digital.receipt.app.auth.dao;

import static com.digital.receipt.app.auth.mapper.AuthenticationMapper.AUTH_MAPPER;

import java.io.IOException;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.abstracts.AbstractSqlDao;
import com.digital.receipt.common.exceptions.InvalidCredentialsException;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

/**
 * Class that handles all the dao calls to the database for users
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
@Repository
public class AuthenticationDao extends AbstractSqlDao {
    /**
     * Not an exposed endpoint, strictly used by the authentication controller to
     * autheticate a user.
     * 
     * @param email    To search for in the database
     * @param password The password to validate against
     * @return User object if the user credentials are correct.
     * @throws SqlFragmentNotFoundException
     * @throws IOException
     */
    public User authenticateUser(String email, String password) throws Exception {
        try {
            return sqlClient.getTemplate(getSql("authenticateUser"),
                    params("email", email).addValue("password", password), AUTH_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidCredentialsException("Invalid Credentials!");
        }
    }
}
