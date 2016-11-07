package com.elo7.nightfall.examples.stream.rddpair.cassandra;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class CassandraRDDPairJob {

	public static void main(String[] args) {
		NightfallApplication.run(CassandraRDDPairJob.class, args);
	}
}
