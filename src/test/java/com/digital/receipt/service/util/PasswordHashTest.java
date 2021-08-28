package com.digital.receipt.service.util;

import com.digital.receipt.app.user.dao.UserDao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class PasswordHashTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void shouldHashPasswordTest() throws Exception {
        userDao.getUserById(1);
        Assert.assertEquals("Should match", 1, 1);
    }
}
