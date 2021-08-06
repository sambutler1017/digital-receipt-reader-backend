package com.digital.receipt.common.enums;

/**
 * Enum to map environments to objects.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public enum Environment {
    PROD(1), DEV(2);

    private int id;

    Environment(int type) {
        this.id = type;
    }

    public int id() {
        return id;
    }

    public static Environment getRole(int id) {
        for (Environment w : Environment.values())
            if (w.id == id)
                return w;
        return DEV;
    }

    public static Environment getRole(String text) {
        for (Environment w : Environment.values())
            if (w.toString().equals(text.toUpperCase()))
                return w;
        return DEV;
    }

    public int getValue() {
        return id;
    }
}
