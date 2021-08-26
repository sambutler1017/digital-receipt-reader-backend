package com.digital.receipt.app.email.client;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.common.abstracts.AbstractClient;

/**
 * Class to expose the email client to other services.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
public class EmailClient extends AbstractClient {

    /**
     * Initialize the Abstract client with the active profile and endpoint path.
     */
    public EmailClient() {
        super("api/mail-app/email");
    }

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     */
    public UserEmail sendEmail(UserEmail userEmail) {
        return post("", userEmail).toEntity(UserEmail.class).block().getBody();
    }

    /**
     * This will send a forgot password link to the given user. If the email exists
     * in the database then the link will be sent.
     * 
     * @param email Email to search for and send an email too.
     */
    public void forgotPassword(String email) throws Exception {
        post("/forgot-password", email).toBodilessEntity().block();
    }
}
