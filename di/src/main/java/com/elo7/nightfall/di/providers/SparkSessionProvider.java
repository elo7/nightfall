package com.elo7.nightfall.di.providers;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.SparkSession;

class SparkSessionProvider implements Provider<SparkSession> {

	private final NightfallConfigurations configurations;

	@Inject
	SparkSessionProvider(NightfallConfigurations configurations) {
		this.configurations = configurations;
	}

	@Override
	public SparkSession get() {
		SparkSession.Builder builder = SparkSession.builder();

		configurations.getProperty("spark.application.name").ifPresent(builder::appName);
		configurations.getProperty("spark.application.master").ifPresent(builder::master);

		return builder.getOrCreate();
	}
}
