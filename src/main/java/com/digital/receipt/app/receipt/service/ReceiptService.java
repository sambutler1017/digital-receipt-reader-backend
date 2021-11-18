package com.digital.receipt.app.receipt.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.client.domain.request.ReceiptGetRequest;
import com.digital.receipt.app.receipt.dao.ReceiptDao;
import com.digital.receipt.jwt.utility.JwtHolder;
import com.digital.receipt.service.cloudinary.ReceiptCloud;
import com.google.common.collect.Sets;

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
     * This will get a list of all the receipts based on the
     * {@link ReceiptGetRequest}
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getReceipts(ReceiptGetRequest request) throws Exception {
        List<Receipt> receipts = dao.getReceipts(request);
        for (Receipt r : receipts) {
            r.setUrl(cloud.getUrl(r.getFilePublicId()));
        }

        return receipts;
    }

    /**
     * This will get all of the currently logged in users receipts.
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    public List<Receipt> getCurrentUserReceipts() throws Exception {
        ReceiptGetRequest r = new ReceiptGetRequest();
        r.setUserId(Sets.newHashSet(jwtHolder.getRequiredUserId()));

        return getReceipts(r);
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
     * This will associate the passed in receipt id to the given user id.
     * 
     * @param receiptId Id of the receipt to associate the user to.
     * @param userId    The user id to associate.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateUserToReceipt(int receiptId, int userId) throws Exception {
        dao.associateUserToReceipt(receiptId, userId);
        return getReceiptById(receiptId);
    }

    /**
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param receiptId Id of the receipt to associate current user too.
     * @return {@link Receipt}
     * @throws Exception
     */
    public Receipt associateCurrentUserToReceipt(int receiptId) throws Exception {
        return associateUserToReceipt(receiptId, jwtHolder.getRequiredUserId());
    }

    /**
     * This will update information for a users association for the given receipt id
     * in in the request body.
     * 
     * @param receipt The receipt to be updated.
     * @return {@link Receipt} of the updated receipt.
     * @throws Exception
     */
    public Receipt updateCurrentUserAssociation(Receipt receipt) throws Exception {
        if (receipt.getLocation() == null && receipt.getLabel() == null)
            throw new Exception("No data provided to update receipt.");

        receipt.setUserId(jwtHolder.getRequiredUserId());
        dao.updateCurrentUserAssociation(receipt);

        return getCurrentUserReceiptById(receipt.getId());
    }

    /**
     * Delete the receipt for the given id.
     * 
     * @param receiptId The id of the receipt to be deleted.
     * @throws Exception
     */
    public void deleteReceipts(int receiptId) throws Exception {
        deleteReceipts(Sets.newHashSet(receiptId));
    }

    /**
     * Delete the receipts for the given list of ids.
     * 
     * @param receiptIds The id of the receipt to be deleted.
     * @throws Exception
     */
    public void deleteReceipts(Set<Integer> receiptIds) throws Exception {
        ReceiptGetRequest request = new ReceiptGetRequest();
        request.setId(receiptIds);

        deleteReceipts(getReceipts(request));
    }

    /**
     * Delete the receipts for the given list. This will remove the association in
     * cloudinary and also the DB record. If an empty list is passed in then it will
     * just return with no deletions.
     * 
     * @param receipts List of receipts to be deleted.
     * @throws Exception
     */
    public void deleteReceipts(List<Receipt> receipts) throws Exception {
        if (receipts.isEmpty()) {
            return;
        }

        dao.deleteReceiptRecords(receipts.stream().map(r -> r.getId()).collect(Collectors.toList()));
        cloud.delete(receipts.stream().map(r -> r.getFilePublicId()).collect(Collectors.toList()));
    }

    /**
     * This will delete all receipts associated to the current user.
     * 
     * @throws Exception If the receipts can't be deleted.
     */
    public void currentUserDeleteAllReceipts() throws Exception {
        deleteAllUserReceipts(jwtHolder.getRequiredUserId());
    }

    /**
     * This will delete all receipts associated to that user id.
     * 
     * @param userId The user id to delete receipts for.
     * @throws Exception If the receipts can't be deleted.
     */
    public void deleteAllUserReceipts(int userId) throws Exception {
        ReceiptGetRequest r = new ReceiptGetRequest();
        r.setUserId(Sets.newHashSet(userId));

        deleteReceipts(getReceipts(r));
    }
}