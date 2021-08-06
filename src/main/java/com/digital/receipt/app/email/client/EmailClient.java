package com.digital.receipt.app.email.client;

import javax.mail.MessagingException;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.app.email.rest.EmailController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to expose the email client to other services.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
@Component
public class EmailClient {

    @Autowired
    private EmailController emailController;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     * @throws MessagingException
     */
    public UserEmail sendEmail(UserEmail userEmail) throws MessagingException {
        return emailController.sendEmail(userEmail);
    }
}
