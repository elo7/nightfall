package com.elo7.nightfall.di.executor;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;
import org.junit.Test;

@Nightfall(ExecutionMode.BATCH)
public class TaskExecutorTest {

	@Test
	public void shouldProcessTestTask() {
		System.setProperty("spark.app.name", "TestTask");
		System.setProperty("spark.master", "local[2]");

		String args[] = {};
		NightfallApplication.run(TaskExecutorTest.class, args);
	}
}