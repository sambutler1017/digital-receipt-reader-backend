package com.digital.receipt.app.receipt.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.receipt.client.domain.Receipt;
import com.digital.receipt.app.receipt.service.ReceiptService;
import com.digital.receipt.common.enums.WebRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * Get the receipt for the given receipt id.
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
     * This will get the user id and receipt id and associate the two together.
     * 
     * @param rec Receipt object containing the user ID and receipt id.
     * @return {@link Receipt}
     * @throws Exception
     */
    @PostMapping(path = "/associate", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public Receipt associateUserToReceipt(@RequestBody Receipt rec) throws Exception {
        return service.associateUserToReceipt(rec);
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
}