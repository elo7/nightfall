package com.elo7.nightfall.examples.stream.rdd.relational;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class RelationalRDDJob {

	public static void main(String[] args) {
		NightfallApplication.run(RelationalRDDJob.class, args);
	}
}
