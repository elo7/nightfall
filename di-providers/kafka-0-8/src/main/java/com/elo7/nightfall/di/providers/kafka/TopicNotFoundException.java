package com.elo7.nightfall.di.providers.kafka;

public class TopicNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TopicNotFoundException(String message) {
		super(message);
	}
}
