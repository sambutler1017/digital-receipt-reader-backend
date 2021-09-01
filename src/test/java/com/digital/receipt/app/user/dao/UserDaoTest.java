package com.digital.receipt.app.user.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import common.annotations.IntegrationTests;

@IntegrationTests
@Sql("insert-test-data.sql")
@Sql(scripts = "clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserDaoTest {
    @Autowired
    private UserDao dao;

    @Test
    public void getUserForIdTest() throws Exception {
        User user = dao.getUserById(1);

        assertEquals("First Name should be User1", "User1", user.getFirstName());
        assertEquals("Last Name should be User1", "Test1", user.getLastName());
        assertEquals("Email should be User1", "user1@mail.com", user.getEmail());
        assertEquals("Web Role should be User1", WebRole.USER, user.getWebRole());
    }

    @Test
    public void throwsExceptionForUserIdNotFound() {
        Exception exception = assertThrows(UserNotFoundException.class, () -> dao.getUserById(1200));
        assertEquals("Exception messages", "User not found for id: 1200", exception.getMessage());
    }
}
