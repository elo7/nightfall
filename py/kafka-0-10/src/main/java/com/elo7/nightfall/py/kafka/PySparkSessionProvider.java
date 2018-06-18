package com.elo7.nightfall.py.kafka;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryListener;

import java.util.Set;

class PySparkSessionProvider implements Provider<SparkSession> {

	private final Set<StreamingQueryListener> reporterListeners;
	static SparkSession session;

	@Inject
	PySparkSessionProvider(Set<StreamingQueryListener> reporterListeners) {
		this.reporterListeners = reporterListeners;
	}

	@Override
	public SparkSession get() {
		reporterListeners.forEach(session.streams()::addListener);

		return session;
	}
}
