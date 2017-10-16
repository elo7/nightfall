package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.Component;
import com.google.inject.Singleton;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Singleton
class ConsoleReporter extends StreamingQueryListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleReporter.class);

	@Override
	public void onQueryStarted(QueryStartedEvent queryStartedEvent) {
		LOGGER.info(
				"Started query event [name: {}, id: {}, runId: {}].",
				queryStartedEvent.name(),
				queryStartedEvent.id(),
				queryStartedEvent.runId());
	}

	@Override
	public void onQueryProgress(QueryProgressEvent queryProgressEvent) {
		LOGGER.info("Query progress: {}.", queryProgressEvent);
	}

	@Override
	public void onQueryTerminated(QueryTerminatedEvent queryTerminatedEvent) {
		LOGGER.info("Query terminated: [id: {}, runId: {}]", queryTerminatedEvent.id(), queryTerminatedEvent.runId());

		if (queryTerminatedEvent.exception().isDefined()) {
			LOGGER.error(
					"Event [id:{}] terminated with errors: {}",
					queryTerminatedEvent.id(),
					queryTerminatedEvent.exception().get());
		}
	}
}
