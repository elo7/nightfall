package com.elo7.nightfall.di.providers.kafka.simple;

import com.google.inject.Provider;
import com.google.inject.Provides;
import org.apache.spark.streaming.kafka.KafkaCluster;
import scala.collection.JavaConversions;

import javax.inject.Inject;
import javax.inject.Singleton;

class KafkaClusterProvider implements Provider<KafkaCluster> {

	private final KafkaSimpleConfiguration configuration;

	@Inject
	KafkaClusterProvider(KafkaSimpleConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	@Provides
	@Singleton
	public KafkaCluster get() {
		return new KafkaCluster(JavaConversions.asScalaMap(
				configuration.params()).toMap(scala.Predef$.MODULE$.conforms()));
	}
}
