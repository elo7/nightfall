package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.executor.TaskExecutor;
import com.google.inject.AbstractModule;
import org.apache.spark.sql.SparkSession;

@ModuleProvider
class PyKafkaModule extends AbstractModule {

	@Override
	protected void configure() {
		binder().bind(TaskExecutor.class).to(PyExecutor.class);
		binder().bind(SparkSession.class).toInstance(PyKafkaLoader.sparkSession());
	}
}
