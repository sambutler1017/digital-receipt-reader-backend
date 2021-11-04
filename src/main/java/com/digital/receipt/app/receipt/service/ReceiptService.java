package com.digital.receipt.app.receipt.service;

import java.util.List;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.dao.ReceiptDao;
import com.digital.receipt.jwt.utility.JwtHolder;

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

    @Autowired
    private JwtHolder jwtHolder;

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
        return dao.getReceiptById(id);
    }

    /**
     * This will get all of the currently logged in users receipts.
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getCurrentUserReceipts() throws Exception {
        return dao.getCurrentUserReceipts(jwtHolder.getRequiredUserId());
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