package com.elo7.nightfall.di.providers.spark.stream;

import com.elo7.nightfall.di.function.Emiter;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.ReporterSender;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public class JavaStreamContextProvider<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Set<StreamTaskProcessor<T>> tasks;
	private final StreamingConfiguration configuration;
	private final StreamSupplier supplier;
	private final Emiter<JavaDStream<String>> emiter;
	private final ReporterSender reporter;
	private final SparkStreamConverter<T> sparkStreamConverter;

	public JavaStreamContextProvider(Set<StreamTaskProcessor<T>> tasks,
									 StreamingConfiguration configuration,
									 StreamSupplier supplier,
									 Emiter<JavaDStream<String>> emiter,
									 ReporterSender reporter,
									 SparkStreamConverter<T> sparkStreamConverter) {
		this.tasks = tasks;
		this.configuration = configuration;
		this.supplier = supplier;
		this.emiter = emiter;
		this.reporter = reporter;
		this.sparkStreamConverter = sparkStreamConverter;
	}

	public JavaStreamingContext getStreamingContext() {
		Optional<String> checkpoint = configuration.getCheckpointDir();

		if (checkpoint.isPresent()) {
			return JavaStreamingContext.getOrCreate(
					checkpoint.get(),
					// necessary cast because ambiguous method
					(Function0<JavaStreamingContext>) () -> createContext());
		} else {
			return createContext();
		}
	}

	@SuppressWarnings("unchecked")
	private JavaStreamingContext createContext() {
		Optional<String> checkpoint = configuration.getCheckpointDir();
		SparkConf sparkConf = new SparkConf();

		sparkConf.set("spark.streaming.stopGracefullyOnShutdown", configuration.isGracefulShutdown());
		sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", configuration.isWriteAheadLog());
		sparkConf.set("spark.streaming.backpressure.enabled", configuration.isBackPressureEnabled());
		sparkConf.set("spark.streaming.receiver.maxRate", configuration.getMaxRatePerSecond());
		sparkConf.set("spark.streaming.kafka.maxRatePerPartition", configuration.getMaxRatePerSecond());

		JavaStreamingContext context = new JavaStreamingContext(sparkConf, configuration.getBatchInterval());
		JavaDStream<String> stream = supplier
				.get(context)
				.persist(StorageLevel.MEMORY_AND_DISK_SER());


		JavaDStream<T> dataPointStream = sparkStreamConverter.convert(stream)
				.persist(StorageLevel.MEMORY_AND_DISK_SER());

		tasks.forEach(task -> task.process(dataPointStream));
		// post processing
		if (emiter != null) {
			emiter.accept(stream);
		}

		stream.foreachRDD(rdd -> {
			reporter.sendReport(rdd.context(), ApplicationType.STREAM);
		});

		checkpoint.ifPresent(context::checkpoint);

		return context;
	}
}
