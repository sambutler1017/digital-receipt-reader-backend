package com.digital.receipt.service.auth.client.domain;

import java.util.Date;

import com.digital.receipt.common.enums.Environment;

/**
 * Authentication token to be used within the app.
 *
 * @author Sam Butler
 * @since July 31, 2021
 */
public class DigitalReceiptToken {

    private String token;

    private Environment environment;

    private Date createDate;

    public DigitalReceiptToken(String t, Environment env, Date creation) {
        token = t;
        environment = env;
        createDate = creation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
