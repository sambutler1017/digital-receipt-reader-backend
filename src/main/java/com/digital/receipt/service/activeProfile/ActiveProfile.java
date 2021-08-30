package com.digital.receipt.service.activeProfile;

import com.digital.receipt.common.enums.Environment;

import org.springframework.stereotype.Service;

/**
 * Used to set and get the property files and active profile for the application
 * based on the current environment the application is running in.
 *
 * @author Sam Butler
 * @since July 22, 2021
 */
@Service
public class ActiveProfile {
    private static final String PROD_ENV_PATH = "/app/src/main";
    private static final String LOCAL_ENV_PATH = "src/main";
    private static final String TEST_ENV_PATH = "src/test";

    /**
     * Method to set the current active profile the application is running in
     */
    public void setPropertyFile() {
        System.setProperty("spring.profiles.active", getEnvironment().toString().toLowerCase());
    }

    /**
     * This method gets the path to the property file based on the environment
     *
     * @return string of the path to the set property file
     */
    public String getPropertyFilePath() {
        return String.format("%s/resources/%s", getEnvironmentUrl(), getAppPropertiesName());
    }

    /**
     * Gets the Test property file path.
     * 
     * @return {@link String} of the property file path.
     */
    public String getLocalTestPropertyFilePath() {
        return String.format("%s/resources/application-test-local.properties", TEST_ENV_PATH);
    }

    /**
     * This method gets the current environment
     *
     * @return string of the environment currently running
     */
    public Environment getEnvironment() {
        if (System.getenv("APP_ENVIRONMENT") != null) {
            return Environment.getEnvrionment(System.getenv("APP_ENVIRONMENT"));
        } else if (System.getProperty("APP_ENVIRONMENT") != null) {
            return Environment.getEnvrionment(System.getProperty("APP_ENVIRONMENT"));
        }
        return Environment.LOCAL;
    }

    /**
     * Gets the application propteries file name.
     * 
     * @return String of the application file name
     */
    public String getAppPropertiesName() {
        return isLocalEnvironment() ? "application-local.properties" : "application-production.properties";
    }

    /**
     * Gets the environment url
     *
     * @return string of the environment url
     */
    public String getEnvironmentUrl() {
        if (getEnvironment().equals(Environment.PRODUCTION)) {
            return PROD_ENV_PATH;
        } else {
            return LOCAL_ENV_PATH;
        }
    }

    /**
     * Gets the test environment url
     *
     * @return string of the test environment url
     */
    public String getTestEnvironmentUrl() {
        return TEST_ENV_PATH;
    }

    /**
     * Gets the signing key for jwt tokens
     * 
     * @return String of the signing key to use.
     */
    public String getSigningKey() {
        if (isLocalEnvironment()) {
            return "local-key";
        } else if (System.getenv("JWT_SIGNING_KEY") != null) {
            return System.getenv("JWT_SIGNING_KEY");
        } else {
            return System.getProperty("JWT_SIGNING_KEY");
        }
    }

    /**
     * Determines if a local environment instance is being run.
     * 
     * @return boolean if the local instance is being run.
     */
    public boolean isLocalEnvironment() {
        return getEnvironment().equals(Environment.LOCAL);
    }

    /**
     * Gets the URI path for the current environment
     * 
     * @return {@link String} of the environment uri.
     */
    public String getUriPath() {
        return isLocalEnvironment() ? Environment.LOCAL.getUri() : Environment.PRODUCTION.getUri();
    }
}
