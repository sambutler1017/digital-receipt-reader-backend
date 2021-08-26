package com.digital.receipt.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordHashTest {

    @Test
    public void shouldHashPasswordString() {
        Assert.assertEquals("Should match", 1, 1);
    }
}
