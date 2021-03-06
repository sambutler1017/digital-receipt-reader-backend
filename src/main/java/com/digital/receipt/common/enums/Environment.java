package com.digital.receipt.common.enums;

/**
 * Enum to map environments to objects.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public enum Environment {
    PRODUCTION("https://digital-receipt-production.herokuapp.com"), LOCAL("http://localhost:8080");

    private String uri;

    Environment(String uri) {
        this.uri = uri;
    }

    public static Environment getEnvrionment(String text) {
        for (Environment w : Environment.values())
            if (w.toString().equals(text.toUpperCase()))
                return w;
        return LOCAL;
    }

    public String getUri() {
        return this.uri;
    }
}
