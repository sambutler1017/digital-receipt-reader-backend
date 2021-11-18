package com.digital.receipt.app.user.client.domain;

import java.time.LocalDate;

import com.digital.receipt.common.enums.WebRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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

    private String password;

    @JsonInclude(Include.NON_DEFAULT)
    private long salt;

    private LocalDate insertDate;

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

    public LocalDate getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDate insertDate) {
        this.insertDate = insertDate;
    }
}
