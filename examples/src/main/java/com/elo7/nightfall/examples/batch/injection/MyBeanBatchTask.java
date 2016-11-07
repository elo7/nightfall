package com.elo7.nightfall.examples.batch.injection;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.injection.MyBean;
import org.apache.spark.api.java.JavaRDD;

import javax.inject.Inject;

@Task
public class MyBeanBatchTask implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final MyBean myBean;

	@Inject
	MyBeanBatchTask(MyBean myBean) {
		this.myBean = myBean;
	}

	@Override
	public void process(JavaRDD<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.foreachPartition(partition -> partition.forEachRemaining(myBean::log));
	}


}
