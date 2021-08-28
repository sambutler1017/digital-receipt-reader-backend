package com.digital.receipt.tests.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Configuration class for running test with datasource.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Configuration
@Profile("test")
public class DataSourceTestConfig {

    @Autowired
    private ActiveProfile activeProfile;

    Properties prop = new Properties();

    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        initProperties();

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(String.format("jdbc:mysql://databasePI.ddnsfree.com/%s?%s", "receipt_db", getDBParams()));
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Gets the username for the database based on the environment.
     * 
     * @return {@link String} of the username to use.
     */
    private String getUsername() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_USERNAME")
                : prop.getProperty("spring.datasource.username");
    }

    /**
     * Gets the password for the database based on the environment.
     * 
     * @return {@link String} of the password to use.
     */
    private String getPassword() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_PASSWORD")
                : prop.getProperty("spring.datasource.password");
    }

    /**
     * Returns the params to be appended to the db url.
     * 
     * @return {@link String} of the db url params.
     */
    private String getDBParams() {
        return "useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Init the properties test file.
     */
    private void initProperties() {
        try (InputStream input = new FileInputStream(activeProfile.getLocalTestPropertyFilePath())) {
            prop.load(input);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
