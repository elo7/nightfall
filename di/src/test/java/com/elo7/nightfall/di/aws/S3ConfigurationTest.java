package com.elo7.nightfall.di.aws;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class S3ConfigurationTest {

	private S3Configuration subject;

	@Before
	public void setup() {
		subject = new S3Configuration();
	}

	@Test
	public void shouldReturnEmptyWhenRegionIsBlank() {
		subject.awsRegion = " ";
		assertFalse(subject.getAwsRegion().isPresent());
	}

	@Test
	public void shouldReturnEmptyWhenRegionIsNull() {
		subject.awsRegion = null;
		assertFalse(subject.getAwsRegion().isPresent());
	}

	@Test
	public void shouldReturnRegionWhenRegionIsNotBlankAndValidRegion() {
		subject.awsRegion = "us-east-1";
		assertTrue(subject.getAwsRegion().isPresent());
		assertEquals(Region.getRegion(Regions.fromName("us-east-1")), subject.getAwsRegion().get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenRegionIsInvalid() {
		subject.awsRegion = "invalid-region";
		subject.getAwsRegion();
		fail();
	}

}
