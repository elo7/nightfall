package com.elo7.nightfall.distcp.task;

import com.elo7.nightfall.di.aws.S3Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class S3ReaderTaskTest {

	@InjectMocks
	private S3ReaderTask subject;

	@Mock
	private S3Configuration s3Configuration;

	@Test
	public void shouldReturnPathWithoutSlashAtTheBeginningWhenPathStartsWithSlash() {
		when(s3Configuration.getS3Path()).thenReturn("/with-slash/");
		String result = subject.getS3Path("2016-05-22");

		assertEquals("with-slash/dt=2016-05-22/", result);
	}

	@Test
	public void shouldReturnPathWithoutSlashAtTheEndWhenPathEndsWithSlash() {
		when(s3Configuration.getS3Path()).thenReturn("with-slash");
		String result = subject.getS3Path("2016-05-22");

		assertEquals("with-slash/dt=2016-05-22/", result);
	}
}
