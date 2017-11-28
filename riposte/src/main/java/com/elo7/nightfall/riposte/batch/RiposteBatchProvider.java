package com.elo7.nightfall.riposte.batch;

import com.elo7.nightfall.riposte.task.RiposteConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Optional;

class RiposteBatchProvider implements Provider<Dataset<Row>> {

	private final SparkSession session;
	private final RiposteConfiguration configuration;

	@Inject
	RiposteBatchProvider(SparkSession session, RiposteConfiguration configuration) {
		this.session = session;
		this.configuration = configuration;
	}

	@Override
	public Dataset<Row> get() {
		DataFrameReader reader = session.
				read()
				.format(configuration.readerFormat())
				.options(configuration.readerOptions());

		configuration.readerPath().ifPresent(reader::load);

		return reader.load();
	}
}
