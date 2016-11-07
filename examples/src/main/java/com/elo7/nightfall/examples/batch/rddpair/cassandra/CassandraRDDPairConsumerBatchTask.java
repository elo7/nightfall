package com.elo7.nightfall.examples.batch.rddpair.cassandra;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.repository.CassandraRepository;
import com.elo7.nightfall.persistence.cassandra.CassandraRDDPairConsumer;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import javax.inject.Inject;

@Task
public class CassandraRDDPairConsumerBatchTask implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final CassandraRDDPairConsumer<String, Long> consumer;
	private final CassandraRepository repository;

	@Inject
	CassandraRDDPairConsumerBatchTask(CassandraRDDPairConsumer<String, Long> consumer, CassandraRepository repository) {
		this.consumer = consumer;
		this.repository = repository;
	}

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		JavaPairRDD<String, Long> counterRDDPair = dataPointsStream
				.filter(DataPointValidator::isValid)
				.mapToPair(dataPoint -> new Tuple2<>(dataPoint.getType(), 1l))
				.reduceByKey((firstCount, secondCount) -> firstCount + secondCount);

		consumer.apply(counterRDDPair, repository::persistMyRDDPairExample);
	}


}
