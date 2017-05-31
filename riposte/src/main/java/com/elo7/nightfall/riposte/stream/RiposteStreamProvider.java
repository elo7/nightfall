package com.elo7.nightfall.riposte.stream;

import com.elo7.nightfall.riposte.task.RiposteConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

class RiposteStreamProvider implements Provider<Dataset<Row>> {

	private final SparkSession session;
	private final RiposteConfiguration configuration;

	@Inject
	RiposteStreamProvider(SparkSession session, RiposteConfiguration configuration) {
		this.session = session;
		this.configuration = configuration;
	}

	@Override
	public Dataset<Row> get() {
		return session.
				readStream()
				.format(configuration.readerFormat())
				.options(configuration.readerOptions())
				.load();
	}
}
