package com.elo7.nightfall.examples.batch.injection;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MyBean.class);

	private final MyReporter reporter;

	@Inject
	public MyBean(MyReporter reporter) {
		this.reporter = reporter;
	}

	public void log(String data) {
		reporter.sendMetric();
		LOGGER.info("######################## \n Processed: {} \n ######################## ", data);
	}
}
