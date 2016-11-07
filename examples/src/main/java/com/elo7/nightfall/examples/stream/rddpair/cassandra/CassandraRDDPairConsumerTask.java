package com.elo7.nightfall.examples.stream.rddpair.cassandra;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.repository.CassandraRepository;
import com.elo7.nightfall.persistence.cassandra.CassandraRDDPairConsumer;
import org.apache.spark.streaming.api.java.JavaDStream;
import scala.Tuple2;

import javax.inject.Inject;

@Task
public class CassandraRDDPairConsumerTask implements StreamTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final CassandraRDDPairConsumer<String, Long> consumer;
	private final CassandraRepository repository;

	@Inject
	CassandraRDDPairConsumerTask(CassandraRDDPairConsumer<String, Long> consumer, CassandraRepository repository) {
		this.consumer = consumer;
		this.repository = repository;
	}

	@Override
	public void process(JavaDStream<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.mapToPair(dataPoint -> new Tuple2<>(dataPoint.getType(), 1l))
				.reduceByKey((firstCount, secondCount) -> firstCount + secondCount)
				.foreachRDD(rdd -> { consumer.apply(rdd, repository::persistMyRDDPairExample); });
	}


}
