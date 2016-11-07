package com.elo7.nightfall.examples.counter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class DataPointTypeCounter implements Serializable {
	private static final long serialVersionUID = 1L;

	private ZonedDateTime createdAt;
	private String messageType;
	private long typeCounter;

	public DataPointTypeCounter withCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public DataPointTypeCounter withMessageType(String messageType) {
		this.messageType = messageType;
		return this;
	}

	public DataPointTypeCounter withTypeCounter(long typeCounter) {
		this.typeCounter = typeCounter;
		return this;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public String getMessageType() {
		return messageType;
	}

	public long getTypeCounter() {
		return typeCounter;
	}

	public Timestamp getCreatedAtTimestamp() {
		return Timestamp.valueOf(createdAt.toLocalDateTime());
	}
}
