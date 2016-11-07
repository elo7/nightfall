package com.elo7.nightfall.examples.stream.rdd.cassandra;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.repository.CassandraRepository;
import com.elo7.nightfall.persistence.cassandra.CassandraRDDConsumer;
import org.apache.spark.streaming.api.java.JavaDStream;

import javax.inject.Inject;

@Task
public class CassandraRDDConsumerTask implements StreamTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final CassandraRDDConsumer<DataPoint<String>> consumer;
	private final CassandraRepository repository;

	@Inject
	CassandraRDDConsumerTask(CassandraRDDConsumer<DataPoint<String>> consumer, CassandraRepository repository) {
		this.consumer = consumer;
		this.repository = repository;
	}

	@Override
	public void process(JavaDStream<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.foreachRDD(rdd -> { consumer.apply(rdd, repository::persistMyRDDExample); });
	}


}
