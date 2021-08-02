package com.digital.receipt.app.auth.service;

import com.digital.receipt.app.auth.dao.AuthenticationDao;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.service.util.PasswordHash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authorization Service takes a user request and checks the values entered for
 * credentials against known values in the database. If correct credentials are
 * passed, it will grant access to the user requested.
 *
 * @author Sam Butler
 * @since August 2, 2021
 */
@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationDao authDao;

    /**
     * Verifies user credentials passed as a JWTRequest
     *
     * @param username - Entered username at login.
     * @param password - Password entered at login.
     * @throws Exception - Throw an exception if the credentials do not match.
     */
    public User verifyUser(String username, String password) throws Exception {
        User authenticatedUser = authDao.authenticateUser(username, PasswordHash.hashPassword(password));

        if (authenticatedUser == null) {
            throw new Exception("Invalid Credentials!");
        }

        return authenticatedUser;
    }
}
