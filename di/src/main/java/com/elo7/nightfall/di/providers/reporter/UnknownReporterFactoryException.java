package com.elo7.nightfall.di.providers.reporter;

public class UnknownReporterFactoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnknownReporterFactoryException(String message) {
		super(message);
	}

	public UnknownReporterFactoryException(String message, Throwable cause) {
		super(message, cause);
	}
}