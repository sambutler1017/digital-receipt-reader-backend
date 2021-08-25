package com.digital.receipt.common.enums;

/**
 * Enum to map environments to objects.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public enum Environment {
    PRODUCTION, LOCAL;

    Environment() {
    }

    public static Environment getEnvrionment(String text) {
        for (Environment w : Environment.values())
            if (w.toString().equals(text.toUpperCase()))
                return w;
        return LOCAL;
    }
}
