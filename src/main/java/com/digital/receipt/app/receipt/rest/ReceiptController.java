package com.digital.receipt.app.receipt.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        return service.insertReceipt(rec.getFileName());
    }

    /**
     * Get the receipt for the given receipt id.
     * 
     * @param id The id of the receipt to get
     * @return {@link Receipt} of the id.
     * @throws Exception
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @HasAccess(WebRole.USER)
    public Receipt getReceiptById(@PathVariable int id) throws Exception {
        return service.getReceiptById(id);
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
}