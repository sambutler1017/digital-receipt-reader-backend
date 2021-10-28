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
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.jwt.utility.JwtTokenUtil;
import com.google.common.collect.Sets;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;

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

    private final String RESET_LINK = "http://www.digital-receipt-reader.com/reset-password/";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
    public void forgotPassword(String email) throws MessagingException, Exception {
        String content = getForgotPasswordContent(email);

        if ("".equals(content)) {
            return;
        } else {
            sendEmail(buildUserEmail("ridgecampusdigitalreceipt@outlook.com", email, "Forgot Password", content), true);
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

    /**
     * This will build out the reset password link that will be sent with the email.
     * If the users email does not exist this method will return an empty string and
     * it will not send an email.
     * 
     * @param email The users email to search for and send an email too.
     * @return {@link String} of the email content with the replaced link.
     * @throws Exception
     */
    private String getForgotPasswordContent(String email) throws Exception {
        String filePath = "src/main/java/com/digital/receipt/app/email/client/domain/ForgotPasswordEmail.html";
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String emailContent = br.lines().collect(Collectors.joining(" "));
        br.close();

        UserGetRequest request = new UserGetRequest();
        request.setEmail(Sets.newHashSet(email));
        List<User> users = userClient.getUsers(request);

        if (users.size() < 1)
            return "";
        else
            return emailContent.replace("::FORGOT_PASSWORD_LINK::", RESET_LINK + getAuthToken(users.get(0)));
    }

    /**
     * Gets the auth token for the given user. They will need this in order to
     * update thier password since they are not logged into the app.
     * 
     * @param user The user to generate an auth token for.
     * @return {@link String} representation of the auth token.
     */
    private String getAuthToken(User user) {
        return jwtTokenUtil.generateToken(user);
    }

}
