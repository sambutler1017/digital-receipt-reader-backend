package com.digital.receipt.app.email.service;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.digital.receipt.app.email.client.domain.UserEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Service class for doing all the dirty work for sending a message.
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     * @throws MessagingException
     */
    public UserEmail sendEmail(UserEmail userEmail) throws MessagingException {
        SimpleMailMessage mail = new SimpleMailMessage();
        MimeMessage mm = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm, true);

        helper.setFrom(userEmail.getFrom());
        helper.setTo(userEmail.getRecipient());
        helper.setSubject(userEmail.getSubject());
        helper.setText("<h3>Hello World!</h3>");

        // mail.setTo(userEmail.getRecipient());
        // mail.setFrom(userEmail.getFrom());
        // mail.setSubject(userEmail.getSubject());
        // mail.setText(userEmail.getBody());
        javaMailSender.send(mm);

        userEmail.setSentDate(new Date());
        return userEmail;
    }

}
