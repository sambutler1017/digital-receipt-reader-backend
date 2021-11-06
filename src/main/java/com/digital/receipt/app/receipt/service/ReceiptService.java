package com.digital.receipt.app.receipt.service;

import java.util.List;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.dao.ReceiptDao;
import com.digital.receipt.jwt.utility.JwtHolder;
import com.digital.receipt.service.cloudinary.ReceiptCloud;

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

    @Autowired
    private ReceiptCloud cloud;

    /**
     * Get the receipt for the given receipt id.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getReceiptById(int id) throws Exception {
        Receipt r = dao.getReceiptById(id);
        r.setUrl(cloud.getUrl(r.getFilePublicId()));
        return r;
    }

    /**
     * This will get all of the currently logged in users receipts.
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getCurrentUserReceipts() throws Exception {
        List<Receipt> receipts = dao.getCurrentUserReceipts(jwtHolder.getRequiredUserId());

        for (Receipt r : receipts) {
            r.setUrl(cloud.getUrl(r.getFilePublicId()));
        }

        return receipts;
    }

    /**
     * Get the receipt for the given receipt id from the current user account.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getCurrentUserReceiptById(int id) throws Exception {
        Receipt r = dao.getCurrentUserReceiptById(id, jwtHolder.getRequiredUserId());
        r.setUrl(cloud.getUrl(r.getFilePublicId()));
        return r;
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

    /**
     * This will get the {@link String} url path that routes to the user receipt.
     * This will take in a public id to get the receipt for. This will throw an
     * error if the receipt public Id does not exist.
     * 
     * @param pid The public Id of the receipt.
     * @return {@link String} url to the receipt.
     * @throws Exception
     */
    public String getPublicIdUrlPath(String pid) throws Exception {
        return cloud.getUrl(pid);
    }

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
     * Delete the receipt for the given id. This is an admin only enpoint. This
     * allows for testing purposes of deleting receipts that are associated to our
     * users.
     * 
     * @param id The id of the receipt to be deleted.
     * @throws Exception
     */
    public void deleteReceipt(int id) throws Exception {
        deleteReceiptRecord(getReceiptById(id));
    }

    /**
     * Delete the receipt for the given id. It will first check to make sure that
     * the receipt belongs to that user. If it does not then it will throw an
     * exception. Otherwise it will continue through the process of unassociating
     * the receipt from the user and removing it from the S3 bucket.
     * 
     * @param id The id of the receipt that needs deleted.
     * @throws Exception
     */
    public void deleteCurrentUserReceipt(int id) throws Exception {
        deleteReceiptRecord(getCurrentUserReceiptById(id));
    }

    /**
     * Private helper method that will delete the public id record in the database
     * and also remove it from the cloudinary S3 bucket.
     * 
     * @param r The receipt to be removed.
     * @throws Exception If the receipt can not be removed or deleted.
     */
    private void deleteReceiptRecord(Receipt r) throws Exception {
        dao.deleteCurrentUserReceipt(r.getId());
        cloud.delete(r.getFilePublicId());
    }
}