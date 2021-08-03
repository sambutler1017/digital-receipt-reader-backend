package com.digital.receipt.app.auth.client;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.auth.client.domain.DigitalReceiptToken;
import com.digital.receipt.app.auth.rest.AuthenticationController;
import com.digital.receipt.jwt.model.AuthenticationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * Client method for authentication of a user.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Client
public class AuthenticationClient {
    @Autowired
    private AuthenticationController authController;

    /**
     * Verifies user credentials passed as a JWTRequest
     *
     * @param username - Entered username at login.
     * @param password - Password entered at login.
     * @throws Exception - Throw an exception if the credentials do not match.
     */
    public ResponseEntity<DigitalReceiptToken> verifyUser(String username, String password) throws Exception {
        return authController.authenticateUser(new AuthenticationRequest(username, password));
    }

    /**
     * Reauthenticates a user and generates a new token.
     *
     * @param authenticationRequest - JWT request. A username and password.
     * @return a new JWT.
     * @throws Exception - If user does not exist.
     */
    public ResponseEntity<DigitalReceiptToken> reauthenticateUser() throws Exception {
        return authController.reauthenticateUser();
    }
}
