package com.digital.receipt.app.receipt.client.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Receipt object to map the receipt data to.
 * 
 * @author Sam butler
 * @since November 1, 2021
 */
public class Receipt {
    private int id;

    @JsonInclude(Include.NON_DEFAULT)
    private int userId;

    private String fileName;

    private Date insertDate;

    public Receipt() {
    }

    public Receipt(String data, Date insertDate) {
        this.fileName = data;
        this.insertDate = insertDate;
    }

    public Receipt(int id, String fileName, Date insertDate) {
        this.id = id;
        this.fileName = fileName;
        this.insertDate = insertDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
}
