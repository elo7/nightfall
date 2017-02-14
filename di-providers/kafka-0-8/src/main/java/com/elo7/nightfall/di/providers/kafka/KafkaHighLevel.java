package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.providers.ExecutorProvider;
import com.elo7.nightfall.di.providers.kafka.highlevel.KafkaHighLevelConfiguration;
import com.elo7.nightfall.di.providers.kafka.highlevel.KafkaHighLevelStreamingContextProvider;
import com.elo7.nightfall.di.providers.spark.stream.SparkStreamConverterModule;
import com.elo7.nightfall.di.providers.spark.stream.StreamingConfiguration;
import com.elo7.nightfall.di.providers.spark.stream.StreamingModuleProvider;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotate the main class with this annotation to read data points from Kafka using Kafka HighLevel API.
 * For configurations see {@link KafkaHighLevelConfiguration}.
 * </p>
 * <p>
 * Kafka HighLevel supports grouping, but is slower then {@link KafkaSimple} when no data loss is expected.
 * To setup streams with no data loss you should configure the stream with writeAheadLog and checkpoints.
 * See {@link StreamingConfiguration}.
 * </p>
 *
 * @see <a href="http://spark.apache.org/docs/latest/streaming-kafka-integration.html#approach-1-receiver-based-approach">Receiver-based Approach</a>
 */
@BindingAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExecutorProvider(
		provider = KafkaHighLevelStreamingContextProvider.class,
		module = StreamingModuleProvider.class,
		additionalModules = {SparkStreamConverterModule.class})
public @interface KafkaHighLevel {
}
