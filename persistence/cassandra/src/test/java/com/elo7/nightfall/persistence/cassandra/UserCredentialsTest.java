package com.elo7.nightfall.persistence.cassandra;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserCredentialsTest {

    private static final String NAME = "name";
    private static final String PASS = "pass";

    @Test
    public void shouldReturnEmptyWhenUserIsBlank() {
        Optional<UserCredentials> user = UserCredentials.createOptional(" ", PASS);

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldPresentEmptyWhenUserIsNotBlankAndPasswordIsBlank() {
        Optional<UserCredentials> optionalUser = UserCredentials.createOptional(NAME, " ");

        assertTrue(optionalUser.isPresent());

        UserCredentials user = optionalUser.get();

        assertEquals(NAME, user.getName());
    }

    @Test
    public void shouldPresentEmptyWhenUserAndPasswordAreNotBlank() {
        Optional<UserCredentials> optionalUser = UserCredentials.createOptional(NAME, PASS);

        assertTrue(optionalUser.isPresent());

        UserCredentials user = optionalUser.get();

        assertEquals(NAME, user.getName());
        assertEquals(PASS, user.getPassword());
    }
}
