package com.elo7.nightfall.examples.batch.rdd.cassandra;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.repository.CassandraRepository;
import com.elo7.nightfall.persistence.cassandra.CassandraRDDConsumer;
import org.apache.spark.api.java.JavaRDD;

import javax.inject.Inject;

@Task
public class CassandraRDDConsumerBatchTask implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final CassandraRDDConsumer<DataPoint<String>> consumer;
	private final CassandraRepository repository;

	@Inject
	CassandraRDDConsumerBatchTask(CassandraRDDConsumer<DataPoint<String>> consumer, CassandraRepository repository) {
		this.consumer = consumer;
		this.repository = repository;
	}

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		JavaRDD<DataPoint<String>> filteredRDD = dataPointsStream
				.filter(DataPointValidator::isValid);

		consumer.apply(filteredRDD, repository::persistMyRDDExample);
	}


}
