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
        System.setProperty("spring.profiles.active", getEnvironment().toString());
    }

    /**
     * This method gets the path to the property file based on the environment
     *
     * @return string of the path to the set property file
     */
    public String getPropertyFilePath() {
        return String.format("%s/resources/application.local.properties", getEnvironmentUrl());
    }

    /**
     * This method gets the current environment
     *
     * @return string of the environment currently running
     */
    public Environment getEnvironment() {
        return System.getenv("APP_ENVIRONMENT") != null ? Environment.PRODUCTION : Environment.LOCAL;
    }

    /**
     * Gets the environment url
     *
     * @return string of the environment url
     */
    public String getEnvironmentUrl() {
        return isLocalEnvironment() ? LOCAL_ENV_PATH : PROD_ENV_PATH;
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
