package com.elo7.nightfall.riposte.stream;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.riposte.task.DatasetConsumer;
import com.elo7.nightfall.riposte.task.Riposte;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@ModuleProvider
class RiposteStreamModule extends AbstractModule {

	private static final TypeLiteral<Dataset<Row>> TYPE = new TypeLiteral<Dataset<Row>>() {
	};

	@Override
	protected void configure() {
		bind(TYPE)
				.annotatedWith(Riposte.class)
				.toProvider(RiposteStreamProvider.class);
		bind(DatasetConsumer.class).to(StreamDatasetConsumer.class);
	}
}
