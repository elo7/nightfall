package com.elo7.nightfall.di.tasks;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import javax.inject.Singleton;
import java.time.ZonedDateTime;
import java.util.Arrays;

class TestRDDProvider implements Provider<JavaRDD<DataPoint<String>>> {

	private final JavaSparkContext context;

	@Inject
	TestRDDProvider(JavaSparkContext context) {
		this.context = context;
	}

	@Override
	@Provides
	@Singleton
	public JavaRDD<DataPoint<String>> get() {
		return context.parallelize(Arrays.asList(create("first"), create("second"), create("third")));
	}

	private DataPoint<String> create(String payload) {
		return new DataPoint<>("type", ZonedDateTime.now(), payload);
	}
}
