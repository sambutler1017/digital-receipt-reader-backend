package com.digital.receipt.app.user.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import common.annotations.IntegrationTests;

@IntegrationTests
public class UserDaoTest {

    @BeforeAll
    public static void setup() throws Exception {
        System.out.println("In Before");
    }

    @DisplayName("Should get the user for the given id")
    @Test
    public void getUserForTheGivenId() throws Exception {
        System.out.println("In Test");
    }
}
