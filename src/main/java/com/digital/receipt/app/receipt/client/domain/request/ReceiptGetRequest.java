package com.digital.receipt.app.receipt.client.domain.request;

import java.util.Set;

/**
 * Request object used to filter out receipts on a get call.
 * 
 * @author Sam Butler
 * @since November 12, 2021
 */
public class ReceiptGetRequest {
    private Set<Integer> id;

    private Set<Integer> userId;

    public Set<Integer> getId() {
        return id;
    }

    public void setId(Set<Integer> id) {
        this.id = id;
    }

    public Set<Integer> getUserId() {
        return userId;
    }

    public void setUserId(Set<Integer> userId) {
        this.userId = userId;
    }
}
