package com.digital.receipt.tests.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.digital.receipt.service.activeProfile.ActiveProfile;

/**
 * Base Test configuration class.
 * 
 * @author Sam Butler
 * @since August 29, 2021
 */
public class BaseTestConfig {
    protected Properties prop;
    protected ActiveProfile activeProfile;

    /**
     * Default constructor for initializing properties file.
     */
    public BaseTestConfig(ActiveProfile activeProfile) {
        this.activeProfile = activeProfile;
        this.prop = new Properties();
        initPropertiesFile();
    }

    /**
     * Gets the username for the database based on the environment.
     * 
     * @return {@link String} of the username to use.
     */
    protected String getUsername() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_USERNAME")
                : prop.getProperty("spring.datasource.username");
    }

    /**
     * Gets the password for the database based on the environment.
     * 
     * @return {@link String} of the password to use.
     */
    protected String getPassword() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_PASSWORD")
                : prop.getProperty("spring.datasource.password");
    }

    /**
     * Returns the params to be appended to the db url.
     * 
     * @return {@link String} of the db url params.
     */
    protected String getDBParams() {
        return "useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Init the properties test file.
     */
    protected void initPropertiesFile() {
        try (InputStream input = new FileInputStream(activeProfile.getLocalTestPropertyFilePath())) {
            prop.load(input);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
