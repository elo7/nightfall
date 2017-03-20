package com.elo7.nightfall.di.providers.kafka.highlevel;

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

public class KafkaHighLevelStreamingContextProvider implements Provider<JavaStreamingContext> {

	private final JavaStreamContextProvider provider;

	@Inject
	KafkaHighLevelStreamingContextProvider(
			Set<StreamTaskProcessor> tasks,
			StreamingConfiguration streamingConfiguration,
			KafkaHighLevelSupplier supplier,
			ReporterSender reporter,
			SparkStreamConverter converter) {
		provider = new JavaStreamContextProvider(tasks, streamingConfiguration, supplier, null, reporter, converter);
	}

	@Override
	@Provides
	@Singleton
	public JavaStreamingContext get() {
		return provider.getStreamingContext();
	}


}
