package com.elo7.nightfall.riposte.task;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public interface DatasetConsumer extends Serializable {

	void consume(Dataset<?> dataset);
}
