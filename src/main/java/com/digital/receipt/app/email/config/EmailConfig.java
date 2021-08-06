package com.digital.receipt.app.email.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.digital.receipt.app.email.service.EmailService;
import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Email config Bean for sending emails to the server. This confirms that it
 * uses the right properties file to pull the information.
 * 
 * @author Sam Butler
 * @since August 6, 2021
 */
@Configuration
public class EmailConfig {
    @Autowired
    private ActiveProfile profile;

    Properties prop = new Properties();

    /**
     * Define email sender with set properties for authentication.
     * 
     * @return {@link JavaMailSender} with the set properties.
     * @see {@link EmailService}
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        try (InputStream input = new FileInputStream(profile.getPropertyFilePath())) {
            prop.load(input);
        } catch (IOException io) {
            io.printStackTrace();
        }

        mailSender.setHost(prop.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(prop.getProperty("spring.mail.port")));
        mailSender.setUsername(prop.getProperty("spring.mail.username"));
        mailSender.setPassword(prop.getProperty("spring.mail.password"));
        mailSender.setProtocol("smtp");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
