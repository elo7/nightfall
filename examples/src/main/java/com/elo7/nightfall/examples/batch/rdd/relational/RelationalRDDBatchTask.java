package com.elo7.nightfall.examples.batch.rdd.relational;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.function.Reducer;
import com.elo7.nightfall.di.function.Transformer;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.counter.DataPointTypeCounter;
import com.elo7.nightfall.examples.counter.DataPointTypeCounterMapper;
import com.elo7.nightfall.examples.counter.DataPointTypeCounterReducer;
import com.elo7.nightfall.examples.injection.MyBean;
import com.elo7.nightfall.examples.repository.RelationalRepository;
import org.apache.spark.api.java.JavaRDD;

import javax.inject.Inject;

@Task
public class RelationalRDDBatchTask implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final MyBean myBean;
	private final Transformer<DataPoint<String>, DataPointTypeCounter> mapper;
	private Reducer<DataPointTypeCounter> reducer;
	private RelationalRepository repository;

	@Inject
	RelationalRDDBatchTask(MyBean myBean, DataPointTypeCounterMapper mapper,
						   DataPointTypeCounterReducer reducer, RelationalRepository repository) {
		this.myBean = myBean;
		this.mapper = mapper;
		this.reducer = reducer;
		this.repository = repository;
	}

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		DataPointTypeCounter dataPointTypeCounter = dataPointsStream
				.filter(DataPointValidator::isValid)
				.map(mapper::apply)
				.reduce(reducer::apply);

		repository.updateDataPointTypeCounter(dataPointTypeCounter);
	}


}
