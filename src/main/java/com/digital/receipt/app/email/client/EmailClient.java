package com.digital.receipt.app.email.client;

import java.io.FileNotFoundException;

import javax.mail.MessagingException;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.app.email.rest.EmailController;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class to expose the email client to other services.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
public class EmailClient {

    @Autowired
    private EmailController controller;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     * @throws MessagingException
     * @throws FileNotFoundException
     */
    public UserEmail sendEmail(UserEmail userEmail) throws FileNotFoundException, MessagingException {
        return controller.sendEmail(userEmail);
    }

    /**
     * This will send a forgot password link to the given user. If the email exists
     * in the database then the link will be sent.
     * 
     * @param email Email to search for and send an email too.
     */
    public void forgotPassword(String email) throws Exception {
        controller.forgotPassword(email);
    }
}
