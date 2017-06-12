package com.elo7.nightfall.examples.batch.simple;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;

@Nightfall(ExecutionMode.BATCH)
public class SimpleBatch {

	public static void main(String[] args) {
		NightfallApplication.run(SimpleBatch.class, args);
	}
}
