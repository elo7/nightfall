package com.elo7.nightfall.examples.batch.rddpair.relational;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class RelationalRDDPairBatchJob {

	public static void main(String[] args) {
		NightfallApplication.run(RelationalRDDPairBatchJob.class, args);
	}
}
