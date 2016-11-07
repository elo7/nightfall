package com.elo7.nightfall.di.executors.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

class ConsoleJobHistory implements JobHistoryRepository {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleJobHistory.class);

	@Override
	public void persistHistory(@Nonnull JobHistory jobHistory) {
		LOGGER.info("Job execution finished: {}.", jobHistory);
	}
}
