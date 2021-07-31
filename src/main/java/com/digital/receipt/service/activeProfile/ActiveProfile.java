package com.digital.receipt.service.activeProfile;

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
    private static final String PROD_ENV = "/app/src/main";
    private static final String LOCAL_ENV = "../digital-receipt-reader-backend/src/main";

    /**
     * Method to set the current active profile the application is running in
     */
    public void setPropertyFile() {
        if (System.getenv("APP_ENVIRONMENT") != null) {
            System.setProperty("spring.profiles.active", System.getenv("APP_ENVIRONMENT"));
        } else {
            System.setProperty("spring.profiles.active", "local");
        }
    }

    /**
     * This method gets the path to the property file based on the environment
     *
     * @return string of the path to the set property file
     */
    public String getPropertyFilePath() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals("production")) {
            return PROD_ENV + "/resources/application.properties";
        } else {
            return LOCAL_ENV + "/resources/application.properties";
        }
    }

    /**
     * This method gets the current environment
     *
     * @return string of the environment currently running
     */
    public String getEnvironment() {
        if (System.getenv("APP_ENVIRONMENT") != null) {
            return System.getenv("APP_ENVIRONMENT");
        } else
            return "local";
    }

    /**
     * Gets the environment url
     *
     * @return string of the environment url
     */
    public String getEnvironmentUrl() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals("production"))
            return PROD_ENV;
        else
            return LOCAL_ENV;
    }

    /**
     * Get the path of the environment url web path
     * 
     * @return String of the web url path
     */
    public String getWebUrl() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null && profile.equals("production"))
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
        if (profile != null && profile.equals("production"))
            return "marcs-microservice.herokuapp.com";
        else
            return "localhost:8080.com";
    }
}
