package com.elo7.nightfall.examples.stream.rdd.cassandra;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class CassandraRDDJob {

	public static void main(String[] args) {
		NightfallApplication.run(CassandraRDDJob.class, args);
	}
}
