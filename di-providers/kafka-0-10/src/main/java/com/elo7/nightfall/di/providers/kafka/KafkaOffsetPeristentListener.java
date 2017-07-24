package com.elo7.nightfall.di.providers.kafka;

import org.apache.spark.sql.streaming.StreamingQueryListener;

public class KafkaOffsetPeristentListener extends StreamingQueryListener {

	private final KafkaOffsetRepository repository;

	KafkaOffsetPeristentListener(KafkaOffsetRepository repository) {
		this.repository = repository;
	}

	@Override
	public void onQueryStarted(QueryStartedEvent queryStartedEvent) {

	}

	@Override
	public void onQueryProgress(QueryProgressEvent queryProgressEvent) {

	}

	@Override
	public void onQueryTerminated(QueryTerminatedEvent queryTerminatedEvent) {

	}
}
