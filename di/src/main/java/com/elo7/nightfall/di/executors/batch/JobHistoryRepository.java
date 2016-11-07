package com.elo7.nightfall.di.executors.batch;

import com.google.inject.ImplementedBy;

import javax.annotation.Nonnull;
import java.io.Serializable;

@ImplementedBy(ConsoleJobHistory.class)
public interface JobHistoryRepository extends Serializable {

	void persistHistory(@Nonnull JobHistory jobHistory);

}
