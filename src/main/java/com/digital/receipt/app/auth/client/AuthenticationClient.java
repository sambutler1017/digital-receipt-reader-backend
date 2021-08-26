package com.digital.receipt.app.auth.client;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.auth.client.domain.DigitalReceiptToken;
import com.digital.receipt.common.abstracts.AbstractClient;
import com.digital.receipt.jwt.model.AuthenticationRequest;
import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * Client method for authentication of a user.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Client
public class AuthenticationClient extends AbstractClient {

    /**
     * Initialize the Abstract client with the active profile and endpoint path.
     */
    @Autowired
    public AuthenticationClient(ActiveProfile activeProfile) {
        super("", activeProfile);
    }

    /**
     * Verifies user credentials passed as a JWTRequest
     *
     * @param email    - Entered email at login.
     * @param password - Password entered at login.
     */
    public ResponseEntity<DigitalReceiptToken> authenticateUser(String email, String password) {
        return post("/authenticate", new AuthenticationRequest(email, password)).toEntity(DigitalReceiptToken.class)
                .block();
    }

    /**
     * Reauthenticates a user and generates a new token.
     *
     * @param authenticationRequest A email and password request.
     * @return a new JWT.
     */
    public ResponseEntity<DigitalReceiptToken> reauthenticateUser() {
        return post("/reauthenticate", null).toEntity(DigitalReceiptToken.class).block();
    }
}
