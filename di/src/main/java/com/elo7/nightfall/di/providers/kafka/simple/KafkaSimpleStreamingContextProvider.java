package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.reporter.ReporterSender;
import com.elo7.nightfall.di.providers.spark.stream.JavaStreamContextProvider;
import com.elo7.nightfall.di.providers.spark.stream.SparkStreamConverter;
import com.elo7.nightfall.di.providers.spark.stream.StreamingConfiguration;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import javax.inject.Singleton;
import java.util.Set;

public class KafkaSimpleStreamingContextProvider implements Provider<JavaStreamingContext> {

	private final JavaStreamContextProvider<?> provider;

	@Inject
	KafkaSimpleStreamingContextProvider(
			Set<StreamTaskProcessor> tasks,
			StreamingConfiguration streamingConfiguration,
			KafkaSimpleStreamSupplier supplier,
			KafkaSimplePostConsumer consumer,
			ReporterSender reporter,
			SparkStreamConverter converter) {
		provider = new JavaStreamContextProvider(tasks, streamingConfiguration, supplier, consumer, reporter, converter);
	}

	@Override
	@Provides
	@Singleton
	public JavaStreamingContext get() {
		return provider.getStreamingContext();
	}
}
