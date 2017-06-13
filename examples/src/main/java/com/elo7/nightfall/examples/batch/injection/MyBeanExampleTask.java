package com.elo7.nightfall.examples.batch.injection;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

@Task
@Singleton
public class MyBeanExampleTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;

	private final SparkSession session;
	private final MyBean myBean;

	@Inject
	public MyBeanExampleTask(MyBean myBean, SparkSession session) {
		this.myBean = myBean;
		this.session = session;
	}

	@Override
	public void process() {
		session.createDataset(Arrays.asList("input 1", "input 2", "invalid input"), Encoders.STRING())
				.foreach(myBean::log);
	}
}
