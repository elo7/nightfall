package com.elo7.nightfall.examples.stream.injection;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.examples.injection.MyBean;
import org.apache.spark.streaming.api.java.JavaDStream;

import javax.inject.Inject;

@Task
public class MyBeanTask implements StreamTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;
	private final MyBean myBean;

	@Inject
	MyBeanTask(MyBean myBean) {
		this.myBean = myBean;
	}

	@Override
	public void process(JavaDStream<DataPoint<String>> dataPointsStream) {
		dataPointsStream
				.filter(DataPointValidator::isValid)
				.foreachRDD(rdd -> {
					if (!rdd.isEmpty()) {
						rdd.foreachPartition(partition -> partition.forEachRemaining(myBean::log));
					}
				});
	}


}
