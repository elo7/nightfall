package com.elo7.nightfall.examples.injection;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;

public class MyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MyBean.class);
	private final DoerOfSomething doerOfSomething;

	@Inject
	MyBean(DoerOfSomething doerOfSomething) {
		this.doerOfSomething = doerOfSomething;
	}

	public void log(DataPoint<String> dataPoint) {
		doerOfSomething.doSomething();
		LOGGER.info("######################## \n Processed: {} \n ######################## ", dataPoint);
	}
}
