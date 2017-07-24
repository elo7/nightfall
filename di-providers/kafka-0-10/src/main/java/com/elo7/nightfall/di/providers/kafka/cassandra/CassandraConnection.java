package com.elo7.nightfall.di.providers.kafka.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Class for holding a Cassandra Cluster and Session, also close both.
 */
public class CassandraConnection implements AutoCloseable {

	private final Session session;
	private final Cluster cluster;

	CassandraConnection(Session session, Cluster cluster) {
		this.session = session;
		this.cluster = cluster;
	}

	public Session getSession() {
		return session;
	}

	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Closes the Session and Cluster connection with Cassandra.
	 */
	@Override
	public void close() {
		if (session != null && !session.isClosed()) {
			session.close();
		}

		if (cluster != null && !cluster.isClosed()) {
			cluster.close();
		}
	}
}
