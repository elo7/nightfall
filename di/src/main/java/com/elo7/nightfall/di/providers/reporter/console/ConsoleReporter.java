package com.elo7.nightfall.di.providers.reporter.console;

import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleReporter extends StreamingQueryListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleReporter.class);

	@Override
	public void onQueryStarted(final QueryStartedEvent queryStartedEvent) {
		LOGGER.info(
				"Started query event [name: {}, id: {}, runId: {}].",
				queryStartedEvent.name(),
				queryStartedEvent.id(),
				queryStartedEvent.runId());
	}

	@Override
	public void onQueryProgress(final QueryProgressEvent queryProgressEvent) {
		LOGGER.info("Query progress: {}.", queryProgressEvent);
	}

	@Override
	public void onQueryTerminated(final QueryTerminatedEvent queryTerminatedEvent) {
		LOGGER.info("Query terminated: [id: {}, runId: {}]", queryTerminatedEvent.id(), queryTerminatedEvent.runId());

		if (queryTerminatedEvent.exception().isDefined()) {
			LOGGER.error(
					"Event [id:{}] terminated with errors: {}",
					queryTerminatedEvent.id(),
					queryTerminatedEvent.exception().get());
		}
	}
}
