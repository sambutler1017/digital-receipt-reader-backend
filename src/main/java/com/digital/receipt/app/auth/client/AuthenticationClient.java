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
    private AuthenticationController controller;

    /**
     * Verifies user credentials passed as a JWTRequest
     *
     * @param email    - Entered email at login.
     * @param password - Password entered at login.
     * @throws Exception
     */
    public ResponseEntity<DigitalReceiptToken> authenticateUser(String email, String password) throws Exception {
        return controller.authenticateUser(new AuthenticationRequest(email, password));
    }

    /**
     * Reauthenticates a user and generates a new token.
     *
     * @param authenticationRequest A email and password request.
     * @return a new JWT.
     * @throws Exception
     */
    public ResponseEntity<DigitalReceiptToken> reauthenticateUser() throws Exception {
        return controller.reauthenticateUser();
    }
}
