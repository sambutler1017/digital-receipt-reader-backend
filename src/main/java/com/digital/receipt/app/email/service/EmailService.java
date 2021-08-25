package com.digital.receipt.app.email.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.digital.receipt.app.email.client.domain.UserEmail;
import com.digital.receipt.app.user.client.UserClient;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.common.exceptions.BaseException;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserClient userClient;

    /**
     * {@link UserEmail} object to send a email too. Default from user will be the
     * admin email.
     * 
     * @param userEmail UserEmail object to get the mail properties from
     * @return {@link UserEmail} object with the time it sent.
     * @throws MessagingException
     * @throws FileNotFoundException
     */
    public UserEmail sendEmail(UserEmail userEmail, boolean html) throws MessagingException, FileNotFoundException {
        MimeMessage mm = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm, true);

        helper.setFrom(userEmail.getFrom());
        helper.setTo(userEmail.getRecipient());
        helper.setSubject(userEmail.getSubject());
        helper.setText(userEmail.getBody(), html);
        javaMailSender.send(mm);

        userEmail.setSentDate(new Date());
        return userEmail;
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
    public User forgotPassword(String email) throws MessagingException, Exception {
        UserGetRequest request = new UserGetRequest();
        request.setEmail(Sets.newHashSet(email));

        List<User> userList = userClient.getUsers(request);

        if (!userList.isEmpty()) {
            String filePath = "src/main/java/com/digital/receipt/app/email/client/domain/ForgotPasswordEmail.html";
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            sendEmail(buildUserEmail("ridgecampusdigitalreceipt@outlook.com", email, "Forgot Password",
                    br.lines().collect(Collectors.joining(" "))), true);
            br.close();
            return userList.get(0);
        } else {
            throw new BaseException(String.format("No user found for email '%s'", email));
        }

    }

    /**
     * Builds out a {@link UserEmail} object.
     * 
     * @param from    Who the email is coming from.
     * @param to      Who the email is going too.
     * @param subject What the main subject of the email is.
     * @param body    What is contained in the email body
     * @return {@link UserEmail}
     * @see #forgotPassword(String)
     */
    private UserEmail buildUserEmail(String from, String to, String subject, String body) {
        UserEmail userEmail = new UserEmail();
        userEmail.setFrom(from);
        userEmail.setRecipient(to);
        userEmail.setSubject(subject);
        userEmail.setBody(body);
        return userEmail;
    }

}
