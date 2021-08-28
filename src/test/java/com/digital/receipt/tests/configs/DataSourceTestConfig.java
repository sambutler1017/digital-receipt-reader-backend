package com.digital.receipt.tests.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.digital.receipt.service.activeProfile.ActiveProfile;
import com.digital.receipt.sql.SqlClient;

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

    /**
     * Create the datasouce config.
     * 
     * @return {@link DataSource} with the set fields.
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        if (System.getProperty("APP_ENVIRONMENT") == null) {
            initPropertiesFile();
        }

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(String.format("jdbc:mysql://databasePI.ddnsfree.com/%s?%s", generateTestSchema(), getDBParams()));
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        return ds;
    }

    /**
     * Config for the jdbcTemplate off of the datasource.
     * 
     * @return {@link JdbcTemplate} with the configured datasource.
     * @throws SQLException
     */
    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Generate the test Schema name and store it.
     * 
     * @return {@link String} of the test schema name.
     */
    private String generateTestSchema() {
        String testSchema = String.format("receipt_test_%d",
                (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

        new SqlClient(getDefaultDataSource()).execute(String.format("CREATE SCHEMA `%s`", testSchema));
        System.setProperty("TEST_SCHEMA_NAME", testSchema);
        return testSchema;
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getDefaultDataSource() {
        return generateDataSource(
                "jdbc:mysql://databasePI.ddnsfree.com/receipt_db?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                getUsername(), getPassword());
    }

    /**
     * Method to generate a datasource object.
     * 
     * @param url      The url to hit the database.
     * @param username Username to access with.
     * @param password Password to access with.
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource generateDataSource(String url, String username, String password) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
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
    private void initPropertiesFile() {
        try (InputStream input = new FileInputStream(activeProfile.getLocalTestPropertyFilePath())) {
            prop.load(input);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
