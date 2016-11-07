package com.elo7.nightfall.examples.batch.rddpair.relational;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.function.Reducer;
import com.elo7.nightfall.di.function.Transformer;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.counter.DataPointTypeCounter;
import com.elo7.nightfall.examples.counter.DataPointTypeCounterReducer;
import com.elo7.nightfall.examples.counter.daily.DataPointTypeCounterDailyMapper;
import com.elo7.nightfall.examples.counter.daily.DataPointTypeCounterKey;
import com.elo7.nightfall.examples.repository.RelationalRepository;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import javax.inject.Inject;

@Task
public class RelationalRDDPairBatchTask implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private Transformer<DataPoint<String>, Tuple2<DataPointTypeCounterKey, DataPointTypeCounter>> mapper;
	private Reducer<DataPointTypeCounter> reducer;
	private RelationalRepository repository;

	@Inject
	RelationalRDDPairBatchTask(DataPointTypeCounterDailyMapper mapper,
							   DataPointTypeCounterReducer reducer,
							   RelationalRepository repository) {
		this.mapper = mapper;
		this.reducer = reducer;
		this.repository = repository;
	}

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.mapToPair(mapper::apply)
				.reduceByKey(reducer::apply)
				.foreachPartition(repository::updateDailyDataPointTypeCounter);
	}

}
