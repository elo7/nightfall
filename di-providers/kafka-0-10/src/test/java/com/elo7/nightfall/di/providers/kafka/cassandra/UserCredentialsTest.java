package com.elo7.nightfall.di.providers.kafka.cassandra;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UserCredentialsTest {

	@Test
	public void shouldCreateEmptyUserWhenNameIsNull() {
		Optional<UserCredentials> result = UserCredentials.createOptional(null, "pass");

		assertEquals(Optional.empty(), result);
	}

	@Test
	public void shouldCreateEmptyUserWhenNameIsBlank() {
		Optional<UserCredentials> result = UserCredentials.createOptional(" ", "pass");

		assertEquals(Optional.empty(), result);
	}

	@Test
	public void shouldCreateUserWhenNameIsNotBlank() {
		Optional<UserCredentials> result = UserCredentials.createOptional("name", "pass");
		UserCredentials userCredentials = result.get();

		assertEquals("name", userCredentials.getName());
		assertEquals("pass", userCredentials.getPassword());
	}
}