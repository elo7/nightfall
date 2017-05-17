package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;

@Nightfall(ExecutionMode.STREAM)
public class KafkaTestMain {

	public static void main(String[] args) {
		System.setProperty("spark.app.name", "TestTask");
		System.setProperty("spark.master", "local[2]");

		NightfallApplication.run(KafkaTestMain.class, args);
	}
}
