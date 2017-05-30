package com.elo7.nightfall.riposte.task;

import com.google.inject.ImplementedBy;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

@ImplementedBy(DefaultDatasetSelect.class)
public interface DatasetSelect extends Serializable {

	Dataset<Row> apply(Dataset<Row> dataset);
}
