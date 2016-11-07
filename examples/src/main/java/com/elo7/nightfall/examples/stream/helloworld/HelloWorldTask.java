package com.elo7.nightfall.examples.stream.helloworld;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Task
public class HelloWorldTask implements StreamTaskProcessor<DataPoint<String>> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldTask.class);

	@Override
	public void process(JavaDStream<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.foreachRDD(rdd -> {
					if (!rdd.isEmpty()) {
						rdd.foreachPartition(partition -> partition.forEachRemaining(this::log));
					}
				});
	}

	private void log(DataPoint<String> dataPoint) {
		LOGGER.info("######################## \n Processed: {} \n ######################## ", dataPoint);
	}
}
