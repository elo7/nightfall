package com.elo7.nightfall.examples.batch.rdd.cassandra;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.file.FileRDD;

@FileRDD
public class CassandraRDDBatchJob {

	public static void main(String[] args) {
		NightfallApplication.run(CassandraRDDBatchJob.class, args);
	}
}
