package com.digital.receipt.app.email.rest;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;

import com.digital.receipt.annotations.interfaces.HasAccess;
import com.digital.receipt.annotations.interfaces.RestApiController;
import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.app.email.service.EmailService;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @HasAccess(WebRole.USER)
    public UserEmail sendEmail(@RequestBody UserEmail userEmail) throws MessagingException, FileNotFoundException {
        userEmail.setFrom("ridgecampusdigitalreceipt@outlook.com");
        return emailService.sendEmail(userEmail, false);
    }

    /**
     * This will send a forgot password link to the given user. If the email exists
     * in the database then the link will be sent.
     * 
     * @param email Email to search for and send an email too.
     * @return {@link User} object of the found user
     * @throws MessagingException           If an error occurs processing the
     *                                      message
     * @throws SqlFragmentNotFoundException The name of the fragment for sql is not
     *                                      found.
     * @throws IOException                  If the forgot password file can not be
     *                                      found.
     */
    @PutMapping("/forgot-password")
    @HasAccess(WebRole.USER)
    public User forgotPassword(@RequestBody String email)
            throws SqlFragmentNotFoundException, MessagingException, IOException {
        return emailService.forgotPassword(email);
    }
}
