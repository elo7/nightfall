package com.elo7.nightfall.riposte.stream;

import com.elo7.nightfall.riposte.task.DatasetConsumer;
import com.elo7.nightfall.riposte.task.RiposteConfiguration;
import com.google.inject.Inject;
import org.apache.spark.sql.Dataset;

class StreamDatasetConsumer implements DatasetConsumer {

	private static final long serialVersionUID = 1L;

	private final RiposteConfiguration configuration;

	@Inject
	StreamDatasetConsumer(RiposteConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void consume(Dataset<?> dataset) {
		dataset
				.writeStream()
				.format(configuration.writerFormat())
				.options(configuration.writerOptions())
				.start();
	}
}
