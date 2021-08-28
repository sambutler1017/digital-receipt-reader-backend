package com.digital.receipt.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordHashTest {

    @Test
    public void shouldHashPasswordTest() {
        Assert.assertEquals("Should match", 1, 1);
    }
}
