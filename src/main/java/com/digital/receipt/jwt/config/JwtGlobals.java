package com.digital.receipt.jwt.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT global variables class
 * 
 * @author Sam butler
 * @since Dec 5, 2020
 */
public class JwtGlobals {

    // Void JWT Endpoints
    public static final Map<String, List<String>> VOID_ENDPOINTS = new HashMap<String, List<String>>() {
        private static final long serialVersionUID = 1L;
        {
            // Authentication Endpoint
            put("/authenticate", Arrays.asList("ANY"));

            // Forgot Password Endpoint in Mail App
            put("/api/mail-app/email/forgot-password", Arrays.asList("POST"));

            // Forgot Password Endpoint in User App
            put("/api/user-app/users/forgot-password", Arrays.asList("POST"));

            // Does User exist endpoint in User App
            put("/api/user-app/users/check-email", Arrays.asList("GET"));
        }
    };

}
