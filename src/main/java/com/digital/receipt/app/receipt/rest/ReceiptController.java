package com.digital.receipt.app.receipt.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Set;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.client.domain.request.ReceiptGetRequest;
import com.digital.receipt.app.receipt.service.ReceiptService;
import com.digital.receipt.common.enums.WebRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Email Controller for dealing with sending emails to users.
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@RequestMapping("api/receipt-app/receipt")
@RestApiController
public class ReceiptController {
    @Autowired
    private ReceiptService service;

    /**
     * Get the receipt for the given receipt id. Only admins are able to make this
     * endpoint call.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public Receipt getReceiptById(@PathVariable int id) throws Exception {
        return service.getReceiptById(id);
    }

    /**
     * This will get a list of all the receipts based on the
     * {@link ReceiptGetRequest}
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public List<Receipt> getReceipts(ReceiptGetRequest request) throws Exception {
        return service.getReceipts(request);
    }

    /**
     * This will get all of the currently logged in users receipts.
     * 
     * @return {@link List<Receipt>} associated to that user.
     * @throws Exception
     */
    @GetMapping(path = "/current-user", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public List<Receipt> getCurrentUserReceipts() throws Exception {
        return service.getCurrentUserReceipts();
    }

    /**
     * Get the receipt for the given receipt id from the current user account.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    @GetMapping(path = "/current-user/{id}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public Receipt getCurrentUserReceiptById(@PathVariable int id) throws Exception {
        return service.getCurrentUserReceiptById(id);
    }

    /**
     * Get the next auto increment value for the receipt details table.
     * 
     * @return {@link Long} of the next auto increment id.
     * @throws Exception
     */
    @GetMapping(path = "/receipt-details/auto-increment", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public long getAutoIncrementReceiptDetails() throws Exception {
        return service.getAutoIncrementReceiptDetails();
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
    @GetMapping(path = "/url/{pid}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public String getPublicIdUrlPath(@PathVariable String pid) throws Exception {
        return service.getPublicIdUrlPath(pid);
    }

    /**
     * This will create the receipt in the database for the given userId and the
     * receipt data to be attatched.
     * 
     * @param receipt UserEmail object to get the mail properties from
     * @return {@link Receipt} of the added receipt.
     * @throws Exception
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public Receipt insertReceipt(@RequestBody Receipt rec) throws Exception {
        return service.insertReceipt(rec.getFilePublicId());
    }

    /**
     * This will associate the passed in receipt id to the given user id.
     * 
     * @param receiptId Id of the receipt to associate the user to.
     * @param userId    The user id to associate.
     * @return {@link Receipt}
     * @throws Exception
     */
    @PostMapping(path = "/{receiptId}/associate/{userId}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.ADMIN)
    public Receipt associateUserToReceipt(@PathVariable int receiptId, @PathVariable int userId) throws Exception {
        return service.associateUserToReceipt(receiptId, userId);
    }

    /**
     * This will associate the current user to the receipt id that is passsed in.
     * 
     * @param receiptId Id of the receipt to associate the user to.
     * @return {@link Receipt}
     * @throws Exception
     */
    @PostMapping(path = "/associate/{receiptId}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public Receipt associateCurrentUserToReceipt(@PathVariable int receiptId) throws Exception {
        return service.associateCurrentUserToReceipt(receiptId);
    }

    /**
     * Delete the receipt for the given id. This is an admin only enpoint. This
     * allows for testing purposes of deleting receipts that are associated to our
     * users.
     * 
     * @param receiptId The id of the receipt to be deleted.
     * @throws Exception
     */
    @DeleteMapping(path = "/{receiptId}")
    @HasAccess(WebRole.ADMIN)
    public void deleteReceipt(@PathVariable int receiptId) throws Exception {
        service.deleteReceipts(receiptId);
    }

    /**
     * Delete the receipts for the given ids. This is an admin only enpoint. This
     * allows for testing purposes of deleting receipts that are associated to our
     * users.
     * 
     * @param id The ids of the receipt to be deleted.
     * @throws Exception
     */
    @DeleteMapping(path = "/bulk")
    @HasAccess(WebRole.ADMIN)
    public void bulkDeleteReceipts(@RequestParam Set<Integer> id) throws Exception {
        service.deleteReceipts(id);
    }

    /**
     * Delete the receipt for the given id. It will first check to make sure that
     * the receipt belongs to that user. If it does not then it will throw an
     * exception. Otherwise it will continue through the process of unassociating
     * the receipt from the user and removing it from the S3 bucket.
     * 
     * @param receiptId The id of the receipt that needs deleted.
     * @throws Exception
     */
    @DeleteMapping(path = "/current-user/{receiptId}")
    @HasAccess(WebRole.USER)
    public void currentUserDeleteReceipt(@PathVariable int receiptId) throws Exception {
        service.deleteReceipts(receiptId);
    }

    /**
     * This will delete all receipts associated to that user.
     * 
     * @throws Exception If the receipts can't be deleted.
     */
    @DeleteMapping(path = "/current-user")
    @HasAccess(WebRole.USER)
    public void currentUserDeleteAllReceipts() throws Exception {
        service.currentUserDeleteAllReceipts();
    }

    /**
     * This will delete all receipts associated to that user id.
     * 
     * @param userId The user id to delete receipts for.
     * @throws Exception If the receipts can't be deleted.
     */
    @DeleteMapping(path = "/user/{userId}")
    @HasAccess(WebRole.ADMIN)
    public void deleteAllUserReceipts(@PathVariable int userId) throws Exception {
        service.deleteAllUserReceipts(userId);
    }
}