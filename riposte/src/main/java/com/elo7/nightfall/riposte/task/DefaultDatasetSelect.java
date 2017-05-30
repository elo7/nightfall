package com.elo7.nightfall.riposte.task;

import com.google.inject.Inject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

class DefaultDatasetSelect implements DatasetSelect {

	private static final long serialVersionUID = -1L;
	private final RiposteConfiguration configuration;

	@Inject
	DefaultDatasetSelect(RiposteConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Dataset<Row> apply(Dataset<Row> dataset) {
		return configuration.query().map(dataset::selectExpr).orElse(dataset);
	}
}
