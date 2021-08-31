package com.digital.receipt.service.util;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import common.annotations.IntegrationTests;

@IntegrationTests
public class PasswordHashTest {

    private static String fakePass;
    private static String hashedPass;

    @BeforeAll
    public static void setup() {
        fakePass = "testPassword!";
        hashedPass = "5bad76f83bb153421cab020bf13d19c364597523ccf616523252e22737be49ec";
    }

    @DisplayName("Should hash the string")
    @Test
    public void shouldHashPasswordTest() throws Exception {
        Assert.assertNotNull(PasswordUtil.hashPassword(fakePass));
    }

    @DisplayName("Password should match sash")
    @Test
    public void passwordShouldMatch() throws Exception {
        Assert.assertTrue("Passwords should match", PasswordUtil.checkPassword(fakePass, hashedPass));
    }

    @DisplayName("Password should not match hash")
    @Test
    public void passwordShouldNotMatch() throws Exception {
        String pass = "nonMatchingPassword";
        Assert.assertFalse("Passwords should not match", PasswordUtil.checkPassword(pass, hashedPass));
    }
}
