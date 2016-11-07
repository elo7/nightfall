package com.elo7.nightfall.examples.batch.rdd.relational;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.file.FileRDD;

@FileRDD
public class RelationalRDDBatchJob {

	public static void main(String[] args) {
		NightfallApplication.run(RelationalRDDBatchJob.class, args);
	}
}
