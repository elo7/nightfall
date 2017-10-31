package com.elo7.nightfall.di.providers.kafka.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CassandraConnectionTest {

	@InjectMocks
	private CassandraConnection subject;

	@Mock
	private Session session;
	@Mock
	private Cluster cluster;

	@Test
	public void shouldNotCloseSessionWhenSessionIsClosed() {
		when(session.isClosed()).thenReturn(true);

		subject.close();

		verify(session, never()).close();
	}

	@Test
	public void shouldCloseSessionWhenSessionIsNotClosed() {
		when(session.isClosed()).thenReturn(false);

		subject.close();

		verify(session).close();
	}

	@Test
	public void shouldNotCloseClusterWhenClusterIsClosed() {
		when(cluster.isClosed()).thenReturn(true);

		subject.close();

		verify(cluster, never()).close();
	}

	@Test
	public void shouldCloseClusterWhenClusterIsNotClosed() {
		when(cluster.isClosed()).thenReturn(false);

		subject.close();

		verify(cluster).close();
	}
}