package com.elo7.nightfall.examples.batch.injection;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.file.FileRDD;

@FileRDD
public class MyBeanBatchJob {

	public static void main(String[] args) {
		NightfallApplication.run(MyBeanBatchJob.class, args);
	}
}
