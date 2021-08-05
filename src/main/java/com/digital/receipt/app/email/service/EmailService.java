package com.digital.receipt.app.email.service;

import java.util.Date;

import com.digital.receipt.app.email.client.domain.UserEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for doing all the dirty work for sending a message.
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     */
    public UserEmail sendEmail(UserEmail userEmail) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(userEmail.getRecipient());
        mail.setFrom(userEmail.getFrom());
        mail.setSubject(userEmail.getSubject());
        mail.setText(userEmail.getBody());
        javaMailSender.send(mail);

        userEmail.setSentDate(new Date());
        return userEmail;
    }

}
