package com.elo7.nightfall.di.examples.stream;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import org.apache.spark.streaming.api.java.JavaDStream;

@Task
public class ExampleStreamTask implements StreamTaskProcessor {

	private static final long serialVersionUID = 1L;

	@Override
    public void process(JavaDStream<DataPoint<String>> stream) {
        stream.foreachRDD(rdd -> {
            rdd.foreachPartition(dataPoints -> {
                dataPoints.forEachRemaining(System.out::println);
            });
            return null;
        });
    }
}
