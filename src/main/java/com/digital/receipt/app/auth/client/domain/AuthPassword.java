package com.digital.receipt.app.auth.client.domain;

/**
 * Auth Password object for storing a hashed password and it's corresponding
 * salt value.
 * 
 * @author Sam Butler
 * @since August 31, 2021
 */
public class AuthPassword {

    private String password;

    private long salt;

    public AuthPassword() {
    }

    public AuthPassword(String password, long salt) {
        this.password = password;
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSalt() {
        return salt;
    }

    public void setSalt(long salt) {
        this.salt = salt;
    }
}
