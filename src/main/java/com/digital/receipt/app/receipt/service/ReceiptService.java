package com.digital.receipt.app.receipt.service;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.dao.ReceiptDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Receipt Service class that handles all service calls to the dao.
 * 
 * @author Sam Butler
 * @since November 1, 2021
 */
@Component
public class ReceiptService {

    @Autowired
    private ReceiptDao dao;

    /**
     * This will create the receipt in the database for the given userId and the
     * receipt data to be attatched.
     * 
     * @param publicId Public key for the file.
     * @return {@link Receipt} of the added receipt.
     * @throws Exception
     */
    public Receipt insertReceipt(String publicId) throws Exception {
        return dao.insertReceipt(publicId);
    }

    /**
     * Get the receipt for the given receipt id.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getReceiptById(int id) throws Exception {
        Receipt rec = dao.getReceiptById(id);
        return rec;
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param rec Receipt object containing the user ID and receipt id.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateUserToReceipt(Receipt rec) throws Exception {
        return dao.associateUserToReceipt(rec);
    }

    /**
     * Get the next auto increment value for the receipt details table.
     * 
     * @return {@link Long} of the next auto increment id.
     * @throws Exception
     */
    public long getAutoIncrementReceiptDetails() throws Exception {
        return dao.getAutoIncrementReceiptDetails();
    }
}