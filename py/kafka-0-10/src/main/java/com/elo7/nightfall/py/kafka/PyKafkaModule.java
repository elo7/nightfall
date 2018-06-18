package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.executor.TaskExecutor;
import com.google.inject.AbstractModule;

@ModuleProvider
class PyKafkaModule extends AbstractModule {

	@Override
	protected void configure() {
		binder().bind(TaskExecutor.class).to(PyKafka.class);
	}
}
