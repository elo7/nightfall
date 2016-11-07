package com.elo7.nightfall.examples.batch.rddpair.cassandra;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.file.FileRDD;

@FileRDD
public class CassandraRDDPairBatchJob {

	public static void main(String[] args) {
		NightfallApplication.run(CassandraRDDPairBatchJob.class, args);
	}
}
