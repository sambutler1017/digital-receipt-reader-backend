package com.digital.receipt.app.auth.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Date;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.auth.client.domain.DigitalReceiptToken;
import com.digital.receipt.app.auth.service.AuthenticationService;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.jwt.model.AuthenticationRequest;
import com.digital.receipt.jwt.utility.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Generates a JWT after being passed a request
 *
 * @author Sam Butler
 * @since August 1, 2021
 */
@RestApiController
public class AuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationService authService;

    /**
     * Generates a JWT token from a request
     *
     * @param authenticationRequest - JWT request. A username and password.
     * @return a new JWT.
     * @throws Exception - if authentication request does not match a user.
     */
    @PostMapping(path = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DigitalReceiptToken> authenticateUser(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        User user = authService.verifyUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity
                .ok(new DigitalReceiptToken(token, new Date(), jwtTokenUtil.getExpirationDateFromToken(token), user));

    }

    /**
     * Reauthenticates a user and generates a new token.
     *
     * @param authenticationRequest - JWT request. A username and password.
     * @return a new JWT.
     * @throws Exception - If user does not exist.
     */
    @PostMapping(path = "/reauthenticate", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public ResponseEntity<DigitalReceiptToken> reauthenticateUser() throws Exception {
        User user = authService.getUserToAuthenticate();
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity
                .ok(new DigitalReceiptToken(token, new Date(), jwtTokenUtil.getExpirationDateFromToken(token), user));
    }
}
