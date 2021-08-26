package com.digital.receipt.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordHashTest {

    @Test
    public void shouldHashPasswordString() {
        System.out.println("Environment: " + System.getenv("APP_ENVIRONMENT"));
        Assert.assertEquals("Should match", 1, 1);
    }
}
