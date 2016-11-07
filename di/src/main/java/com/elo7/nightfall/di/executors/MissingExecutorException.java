package com.elo7.nightfall.di.executors;

public class MissingExecutorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingExecutorException() {
        super("Missing ExecutorProvider annotation on source class.");
    }
}
