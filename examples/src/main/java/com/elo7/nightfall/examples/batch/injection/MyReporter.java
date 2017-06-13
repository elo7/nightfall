package com.elo7.nightfall.examples.batch.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyReporter implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyReporter.class);

	public void sendMetric() {
		LOGGER.info("Metric sent");
	}
}
