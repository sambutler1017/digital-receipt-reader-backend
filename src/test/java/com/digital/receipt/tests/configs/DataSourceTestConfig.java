package com.digital.receipt.tests.configs;

import java.sql.SQLException;

import javax.sql.DataSource;

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
    private static final String TEST_ENV_PATH = "../digital-receipt-reader-backend/src/test";
    // Properties prop = new Properties();

    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        // if (System.getProperty("APP_ENVIRONMENT") == null) {
        // initPropertiesFile();
        // }

        System.out.println("Environment: " + System.getProperty("APP_ENVIRONMENT"));
        System.out.println("Username: " + System.getProperty("MYSQL_USERNAME"));
        System.out.println("Password: " + System.getProperty("MYSQL_PASSWORD"));

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
                : "spring.datasource.username";
    }

    /**
     * Gets the password for the database based on the environment.
     * 
     * @return {@link String} of the password to use.
     */
    private String getPassword() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_PASSWORD")
                : "spring.datasource.password";
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
    // private void initPropertiesFile() {
    // try (InputStream input = new FileInputStream(
    // String.format("%s/resources/application-test-local.properties",
    // TEST_ENV_PATH))) {
    // prop.load(input);
    // } catch (IOException io) {
    // // Continue
    // }
    // }
}
