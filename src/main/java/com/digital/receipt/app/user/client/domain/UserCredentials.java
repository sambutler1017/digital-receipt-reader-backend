package com.digital.receipt.app.user.client.domain;

/**
 * User credential object that holds login information of the user.
 * 
 * @author Sam Butler
 * @since August 10, 2021
 */
public class UserCredentials {
    private int userId;

    private String password;

    private boolean forgotPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(boolean forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

}
