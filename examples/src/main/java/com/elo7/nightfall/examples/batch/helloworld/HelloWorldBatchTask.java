package com.elo7.nightfall.examples.batch.helloworld;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Task
public class HelloWorldBatchTask implements BatchTaskProcessor<DataPoint<String>> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldBatchTask.class);

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.foreachPartition(partition -> partition.forEachRemaining(this::log));
	}

	private void log(DataPoint<String> dataPoint) {
		LOGGER.info("######################## \n Processed: {} \n ######################## ", dataPoint);
	}
}
