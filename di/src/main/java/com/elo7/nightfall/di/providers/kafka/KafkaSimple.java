package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.providers.ExecutorProvider;
import com.elo7.nightfall.di.providers.kafka.simple.KafkaSimpleConfiguration;
import com.elo7.nightfall.di.providers.kafka.simple.KafkaSimpleModule;
import com.elo7.nightfall.di.providers.kafka.simple.KafkaSimpleStreamingContextProvider;
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
 * Annotate the main class with this annotation to read data points from Kafka using Kafka Simple API.
 * For configurations see {@link KafkaSimpleConfiguration}.
 * </p>
 * <p>
 * Kafka Simple do <b>NOT</b> supports grouping, but is faster then {@link KafkaHighLevel} when no data loss is expected.
 * To setup streams with no data loss you should configure the stream with checkpoints.
 * See {@link StreamingConfiguration}.
 * </p>
 *
 * @see <a href="http://spark.apache.org/docs/latest/streaming-kafka-integration.html#approach-2-direct-approach-no-receivers">Direct Approach (No Receivers)</a>
 */
@BindingAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExecutorProvider(
		provider = KafkaSimpleStreamingContextProvider.class,
		module = StreamingModuleProvider.class,
		additionalModules = {KafkaSimpleModule.class, SparkStreamConverterModule.class})
public @interface KafkaSimple {
}
