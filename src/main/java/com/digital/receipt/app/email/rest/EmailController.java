package com.digital.receipt.app.email.rest;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.app.email.service.EmailService;
import com.digital.receipt.common.enums.WebRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Email Controller for dealing with sending emails to users.
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@RequestMapping("api/mail-app/email")
@RestApiController
public class EmailController {
    @Autowired
    private EmailService emailService;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     * @throws MessagingException
     * @throws FileNotFoundException
     */
    @PostMapping()
    @HasAccess(WebRole.ADMIN)
    public UserEmail sendEmail(@RequestBody UserEmail userEmail) throws MessagingException, FileNotFoundException {
        userEmail.setFrom("ridgecampusdigitalreceipt@outlook.com");
        return emailService.sendEmail(userEmail, false);
    }

    /**
     * This will send a forgot password link to the given user. If the email exists
     * in the database then the link will be sent.
     * 
     * @param email Email to search for and send an email too.
     * @throws Exception
     */
    @PostMapping("/forgot-password")
    @HasAccess(WebRole.ADMIN)
    public void forgotPassword(@RequestBody String email) throws Exception {
        emailService.forgotPassword(email);
    }
}
