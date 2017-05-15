package com.elo7.nightfall.di.task;

import com.google.inject.ImplementedBy;

import java.io.Serializable;

@ImplementedBy(DefaultTaskExecutor.class)
public interface TaskExecutor extends Serializable {

	void runTasks();
}
