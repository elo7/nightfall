package com.elo7.nightfall.riposte.batch;

import com.elo7.nightfall.riposte.task.DatasetConsumer;
import com.elo7.nightfall.riposte.task.RiposteConfiguration;
import com.google.inject.Inject;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;

import java.util.Optional;

class BatchDatasetConsumer implements DatasetConsumer {

	private final RiposteConfiguration configuration;

	@Inject
	BatchDatasetConsumer(RiposteConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void consume(Dataset<?> dataset) {
		// Workaround as console format is only supported with Structure Streaming
		if (configuration.writerFormat().equalsIgnoreCase("console")) {
			dataset.show();
		} else {
			DataFrameWriter<?> writer = dataset
					.write()
					.format(configuration.writerFormat())
					.options(configuration.writerOptions());

			Optional<String> writePath = configuration.writerPath();

			if (writePath.isPresent()) {
				writer.save(writePath.get());
			} else {
				writer.save();
			}
		}
	}
}
