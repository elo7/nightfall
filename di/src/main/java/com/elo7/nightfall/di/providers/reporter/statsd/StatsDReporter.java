package com.elo7.nightfall.di.providers.reporter.statsd;

import com.elo7.nightfall.di.Component;
import com.google.inject.Inject;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.apache.spark.sql.streaming.StreamingQueryProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
class StatsDReporter extends StreamingQueryListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatsDReporter.class);

	private final StatsD statsD;

	@Inject
	StatsDReporter(StatsD statsD) throws IOException {
		this.statsD = statsD;
	}

	@Override
	public void onQueryStarted(QueryStartedEvent event) {
		LOGGER.debug("Query Started, nothing to report");
	}

	@Override
	public void onQueryProgress(QueryProgressEvent event) {
		StreamingQueryProgress progress = event.progress();

		progress.durationMs().forEach((key, duration) -> statsD.timing("duration." + key, duration));
		statsD.gauges("inputRowsPerSecond", Math.round(progress.inputRowsPerSecond()));
		statsD.gauges("processedRowsPerSecond", Math.round(progress.processedRowsPerSecond()));
		statsD.gauges("numInputRows", progress.numInputRows());
		// Ensure all data was sent
		statsD.flush();
	}

	@Override
	public void onQueryTerminated(QueryTerminatedEvent event) {
		LOGGER.debug("Query terminated, nothing to report");
	}
}
