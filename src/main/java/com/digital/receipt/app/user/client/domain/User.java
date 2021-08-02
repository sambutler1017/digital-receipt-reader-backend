package com.digital.receipt.app.user.client.domain;

import java.util.Date;

import com.digital.receipt.common.enums.WebRole;

/**
 * Class to create a user profile object
 * 
 * @author Sam Butler
 * @since June 25, 2021
 */
public class User {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private WebRole webRole;

    private Date insertDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public WebRole getWebRole() {
        return webRole;
    }

    public void setWebRole(WebRole webRole) {
        this.webRole = webRole;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
}
