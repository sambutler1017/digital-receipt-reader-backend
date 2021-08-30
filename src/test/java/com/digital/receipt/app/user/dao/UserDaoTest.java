package com.digital.receipt.app.user.dao;

import com.digital.receipt.app.user.client.domain.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import common.annotations.IntegrationTests;

@IntegrationTests
@Sql("insert-test-data.sql")
@Sql(scripts = "clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void getUserForTheGivenId() throws Exception {
        User user = userDao.getUserById(1);
    }
}
