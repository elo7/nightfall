package com.elo7.nightfall.examples.batch.helloworld;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.file.FileRDD;

@FileRDD
public class HelloWorldBatchJob {
	public static void main(String[] args) {
		NightfallApplication.run(HelloWorldBatchJob.class, args);
	}
}
