package com.digital.receipt.app.user.client.domain.request;

import java.util.Set;

/**
 * Get Request used for parameters to controller method.
 *
 * @author Sam Butler
 * @since July 30, 2021
 */
public class UserGetRequest {
    private Set<Integer> id;

    private Set<String> email;

    private Set<String> firstName;

    private Set<String> lastName;

    public Set<Integer> getId() {
        return id;
    }

    public void setId(Set<Integer> id) {
        this.id = id;
    }

    public Set<String> getEmail() {
        return email;
    }

    public void setEmail(Set<String> email) {
        this.email = email;
    }

    public Set<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Set<String> firstName) {
        this.firstName = firstName;
    }

    public Set<String> getLastName() {
        return lastName;
    }

    public void setLastName(Set<String> lastName) {
        this.lastName = lastName;
    }

}
