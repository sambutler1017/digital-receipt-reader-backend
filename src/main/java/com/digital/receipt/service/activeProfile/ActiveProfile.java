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
    private static final String LOCAL_ENV_PATH = "../digital-receipt-reader-backend/src/main";

    /**
     * Method to set the current active profile the application is running in
     */
    public void setPropertyFile() {
        if (System.getenv("APP_ENVIRONMENT") == null) {
            System.setProperty("spring.profiles.active",
                    Environment.getRole(System.getenv("APP_ENVIRONMENT")).toString());
        } else {
            System.setProperty("spring.profiles.active", Environment.LOCAL.toString());
        }
    }

    /**
     * This method gets the path to the property file based on the environment
     *
     * @return string of the path to the set property file
     */
    public String getPropertyFilePath() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals(Environment.PRODUCTION.toString())) {
            return String.format("%s/resources/application.properties", PROD_ENV_PATH);
        } else {
            return String.format("%s/resources/application.local.properties", LOCAL_ENV_PATH);
        }
    }

    /**
     * This method gets the current environment
     *
     * @return string of the environment currently running
     */
    public Environment getEnvironment() {
        if (System.getenv("APP_ENVIRONMENT") != null) {
            return Environment.getRole(System.getenv("APP_ENVIRONMENT").toString());
        } else
            return Environment.LOCAL;
    }

    /**
     * Gets the environment url
     *
     * @return string of the environment url
     */
    public String getEnvironmentUrl() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals(Environment.PRODUCTION.toString()))
            return PROD_ENV_PATH;
        else
            return LOCAL_ENV_PATH;
    }

    /**
     * Get the path of the environment url web path
     * 
     * @return String of the web url path
     */
    public String getWebUrl() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals(Environment.PRODUCTION.toString()))
            return "https://marcs-app.herokuapp.com";
        else
            return "localhost:8080";
    }

    /**
     * Get the path of the environment url microservice path
     * 
     * @return String of the microservice url path
     */
    public String getMicroserviceUrl() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals(Environment.PRODUCTION.toString()))
            return "marcs-microservice.herokuapp.com";
        else
            return "localhost:8080.com";
    }

    /**
     * Determines if a local environment instance is being run.
     * 
     * @return boolean if the local instance is being run.
     */
    public boolean isLocalEnvironment() {
        return getEnvironment().equals(Environment.LOCAL);
    }
}
