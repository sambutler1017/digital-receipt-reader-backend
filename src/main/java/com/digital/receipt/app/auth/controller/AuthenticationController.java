package com.digital.receipt.app.auth.controller;

import java.util.Date;

import com.digital.receipt.app.auth.client.domain.DigitalReceiptToken;
import com.digital.receipt.app.auth.service.AuthenticationService;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.jwt.model.JwtRequest;
import com.digital.receipt.jwt.utility.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Generates a JWT after being passed a request
 *
 * @author Sam Butler
 * @since August 1, 2021
 */
@CrossOrigin
@RestController
public class AuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationService auth;

    /**
     * Generates a JWT token from a request
     *
     * @param authenticationRequest - JWT request. A username and password.
     * @return a new JWT.
     * @throws Exception - if authentication request does not match a user.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<DigitalReceiptToken> createAuthToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {
        User user = auth.verifyUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity
                .ok(new DigitalReceiptToken(token, new Date(), jwtTokenUtil.getExpirationDateFromToken(token), user));

    }

}
