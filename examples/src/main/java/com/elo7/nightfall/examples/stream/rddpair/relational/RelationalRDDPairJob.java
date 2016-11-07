package com.elo7.nightfall.examples.stream.rddpair.relational;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class RelationalRDDPairJob {

	public static void main(String[] args) {
		NightfallApplication.run(RelationalRDDPairJob.class, args);
	}
}
