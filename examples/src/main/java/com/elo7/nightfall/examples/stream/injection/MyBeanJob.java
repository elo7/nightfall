package com.elo7.nightfall.examples.stream.injection;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class MyBeanJob {

	public static void main(String[] args) {
		NightfallApplication.run(MyBeanJob.class, args);
	}
}
