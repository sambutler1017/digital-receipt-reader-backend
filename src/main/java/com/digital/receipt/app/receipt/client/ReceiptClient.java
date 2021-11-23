package com.digital.receipt.app.receipt.client;

import java.util.List;
import java.util.Set;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.client.domain.request.ReceiptGetRequest;
import com.digital.receipt.app.receipt.rest.ReceiptController;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class exposes the user endpoint's to other app's to pull data across the
 * platform.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
public class ReceiptClient {
    @Autowired
    private ReceiptController controller;

    /**
     * Get the receipt for the given receipt id. Only admins are able to make this
     * endpoint call.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getReceiptById(int id) throws Exception {
        return controller.getReceiptById(id);
    }

    /**
     * This will get all of the currently logged in users receipts.
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getCurrentUserReceipts(ReceiptGetRequest request) throws Exception {
        return controller.getCurrentUserReceipts(request);
    }

    /**
     * Get the receipt for the given receipt id from the current user account.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    public Receipt getCurrentUserReceiptById(int id) throws Exception {
        return controller.getCurrentUserReceiptById(id);
    }

    /**
     * Get the next auto increment value for the receipt details table.
     * 
     * @return {@link Long} of the next auto increment id.
     * @throws Exception
     */
    public long getAutoIncrementReceiptDetails() throws Exception {
        return controller.getAutoIncrementReceiptDetails();
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
        return controller.getPublicIdUrlPath(pid);
    }

    /**
     * This will create the receipt in the database for the given userId and the
     * receipt data to be attatched.
     * 
     * @param receipt UserEmail object to get the mail properties from
     * @return {@link Receipt} of the added receipt.
     * @throws Exception
     */
    public Receipt insertReceipt(Receipt rec) throws Exception {
        return controller.insertReceipt(rec);
    }

    /**
     * This will associate the passed in receipt id to the given user id.
     * 
     * @param receiptId Id of the receipt to associate the user to.
     * @param userId    The user id to associate.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateUserToReceipt(int receiptId, int userId) throws Exception {
        return controller.associateUserToReceipt(receiptId, userId);
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param rec Receipt object containing the user ID and receipt id.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateCurrentUserToReceipt(int receiptId) throws Exception {
        return controller.associateCurrentUserToReceipt(receiptId);
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
        controller.deleteReceipt(id);
    }

    /**
     * Delete the receipt for the given id. This is an admin only enpoint. This
     * allows for testing purposes of deleting receipts that are associated to our
     * users.
     * 
     * @param id The id of the receipt to be deleted.
     * @throws Exception
     */
    public void deleteBulkReceiptRecords(Set<Integer> id) throws Exception {
        controller.bulkDeleteReceipts(id);
    }

    /**
     * This will delete all receipts associated to that user id.
     * 
     * @param userId The user id to delete receipts for.
     * @throws Exception If the receipts can't be deleted.
     */
    public void deleteAllUserReceipts(int userId) throws Exception {
        controller.deleteAllUserReceipts(userId);
    }
}
