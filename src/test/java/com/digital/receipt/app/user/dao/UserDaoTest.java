package com.digital.receipt.app.user.dao;

import com.digital.receipt.app.user.client.domain.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import common.annotations.IntegrationTests;

@IntegrationTests
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void getUserForTheGivenId() throws Exception {
        User user = userDao.getUserById(1);
    }
}
