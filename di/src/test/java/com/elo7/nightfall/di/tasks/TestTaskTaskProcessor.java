package com.elo7.nightfall.di.tasks;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import org.apache.spark.api.java.JavaRDD;

@Task
public class TestTaskTaskProcessor implements BatchTaskProcessor<DataPoint<String>> {

	private static final long serialVersionUID = 1L;

	@Override
	public void process(JavaRDD<DataPoint<String>> rdd) {
		rdd.collect().forEach(System.out::println);
	}
}
