package com.elo7.nightfall.di.providers.kafka;

import com.google.inject.Provider;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaProviderTest {

	private KafkaProvider subject;

	@Mock
	private Map<String, String> configurations;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private SparkSession session;
	@Mock
	private Provider<KafkaOffsetPersistentListener> offsetListenerProvider;
	@Mock
	private Provider<KafkaOffsetRepository> repositoryProvider;

	@Before
	public void setup() {
		subject = new KafkaProvider(configurations, session, offsetListenerProvider, repositoryProvider);
	}

	@Test
	public void shouldNotBuildWithRepositoryWhenPersistentOffsetIsDisabled() {
		when(configurations.get("persistent.offsets")).thenReturn("false");

		subject.get();

		verify(repositoryProvider, never()).get();
		verify(offsetListenerProvider, never()).get();
	}

	@Test
	public void shouldBuildWithRepositoryWhenPersistentOffsetEnabled() {
		when(configurations.get("persistent.offsets")).thenReturn("true");

		subject.get();

		verify(repositoryProvider).get();
		verify(offsetListenerProvider).get();
	}
}