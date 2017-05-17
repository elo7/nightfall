package com.elo7.nightfall.di.providers.reporter;

import org.apache.spark.sql.streaming.StreamingQueryListener;

public class DisabledReporter extends StreamingQueryListener {

	@Override
	public void onQueryStarted(final QueryStartedEvent queryStartedEvent) {

	}

	@Override
	public void onQueryProgress(final QueryProgressEvent queryProgressEvent) {

	}

	@Override
	public void onQueryTerminated(final QueryTerminatedEvent queryTerminatedEvent) {

	}
}
