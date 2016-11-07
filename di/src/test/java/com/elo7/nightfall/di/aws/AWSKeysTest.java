package com.elo7.nightfall.di.aws;

import org.junit.Test;

import java.util.Optional;

import static com.elo7.nightfall.di.aws.AWSKeys.create;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AWSKeysTest {

	@Test
	public void shouldReturnAbsentWhenAccessKeyIsMiggingAndSecretKeyIsPresent() {
		Optional<AWSKeys> subject = create(" ", "secret");
		assertFalse(subject.isPresent());
	}

	@Test
	public void shouldReturnAbsentWhenSecretKeyIsMiggingAndAccessKeyIsPresent() {
		Optional<AWSKeys> subject = create("access", " ");
		assertFalse(subject.isPresent());
	}

	@Test
	public void shouldReturnPresentWhenAccessKeyAndSecretKeyIsPresent() {
		Optional<AWSKeys> subject = create("access", "secret");
		assertTrue(subject.isPresent());
	}
}
