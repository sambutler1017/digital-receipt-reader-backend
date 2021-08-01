package com.digital.receipt.service.auth.service;

import java.util.HashSet;
import java.util.List;

import com.digital.receipt.app.user.client.UserClient;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.service.util.PasswordHash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authorization Service takes a user request and checks the values entered for
 * credentials against known values in the database. If correct credentials are
 * passed, it will grant access to the user requested.
 *
 * @author Kiyle Winborne
 * @since 8/2/2020
 */
@Service
public class AuthenticationService {
    @Autowired
    private UserClient userClient;

    /**
     * Verifies user credentials passed as a JWTRequest
     *
     * @param username - Entered username at login.
     * @param password - Password entered at login.
     * @return requestedUser - the user which matches the credentials above.
     * @throws Exception - Throw an exception if the credentials do not match.
     */
    public User verifyUser(String username, String password) throws Exception {
        UserGetRequest request = new UserGetRequest();

        HashSet<String> usernameSet = new HashSet<String>();
        usernameSet.add(username);

        request.setEmail(usernameSet);
        List<User> userList = userClient.getUsers(request);

        if (userList.isEmpty()) {
            throw new Exception("Invalid Credentials!");
        }

        if (!PasswordHash.checkPassword(password, userList.get(0).getPassword())) {
            throw new Exception("Invalid Credentials!");
        }
        return userList.get(0);
    }
}
