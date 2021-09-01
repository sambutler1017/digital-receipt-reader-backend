package com.digital.receipt.app.auth.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.common.enums.WebRole;
import com.digital.receipt.common.exceptions.InvalidCredentialsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import common.annotations.IntegrationTests;

@IntegrationTests
@Sql("insert-test-data.sql")
@Sql(scripts = "clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationDaoTest {
    @Autowired
    private AuthenticationDao dao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authenticateUser() throws Exception {
        String email = "user1@mail.com";
        String password = "fa1e2d102c06ecd5d6d4dbfb4c9ddeca01c3e890d58ffcaf1b04b9afe4590e78";

        User user = dao.authenticateUser(email, password);

        assertEquals("First Name should be User1", "User1", user.getFirstName());
        assertEquals("Last Name should be User1", "Test1", user.getLastName());
        assertEquals("Email should be User1", "user1@mail.com", user.getEmail());
        assertEquals("Web Role should be User1", WebRole.USER, user.getWebRole());
        assertFalse(user.isForgotPassword(), "Forgot password should be false");
    }

    @Test
    public void shouldNotAuthenticateUser() throws Exception {
        String email = "user1@mail.com";
        String password = "incorrectPassword!";

        Exception exception = assertThrows(InvalidCredentialsException.class,
                () -> dao.authenticateUser(email, password));
        assertEquals("Exception messages", "Invalid Credentials!", exception.getMessage());
    }
}
