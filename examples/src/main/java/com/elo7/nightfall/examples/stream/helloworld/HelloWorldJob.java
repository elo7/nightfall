package com.elo7.nightfall.examples.stream.helloworld;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class HelloWorldJob {

	public static void main(String[] args) {
		NightfallApplication.run(HelloWorldJob.class, args);
	}
}
