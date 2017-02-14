package com.elo7.nightfall.di.providers.kafka;

public class InvalidTopicConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTopicConfigurationException(String message) {
		super(message);
	}

	public InvalidTopicConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
