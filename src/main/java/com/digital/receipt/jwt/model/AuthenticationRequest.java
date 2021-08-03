package com.digital.receipt.jwt.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * JWT Request model.
 *
 * @author Seth Hancock
 * @since August 1, 2020
 */
public class AuthenticationRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005169420L;

    @NotNull
    private String username;

    @NotNull
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
