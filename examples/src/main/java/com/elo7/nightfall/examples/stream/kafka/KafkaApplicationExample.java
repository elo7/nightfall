package com.elo7.nightfall.examples.stream.kafka;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;

@Nightfall(ExecutionMode.STREAM)
public class KafkaApplicationExample {

	public static void main(String[] args) {
		NightfallApplication.run(KafkaApplicationExample.class, args);
	}
}
