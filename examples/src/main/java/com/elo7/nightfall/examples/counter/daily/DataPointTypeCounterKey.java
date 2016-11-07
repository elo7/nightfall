package com.elo7.nightfall.examples.counter.daily;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class DataPointTypeCounterKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private final ZonedDateTime createdAt;
	private final String messageType;

	public DataPointTypeCounterKey(ZonedDateTime createdAt, String messageType) {
		this.createdAt = createdAt;
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	public Timestamp getCreatedAtTimestamp() {
		return Timestamp.valueOf(createdAt.toLocalDateTime());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DataPointTypeCounterKey that = (DataPointTypeCounterKey) o;
		return Objects.equal(createdAt, that.createdAt) &&
				Objects.equal(messageType, that.messageType);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(createdAt, messageType);
	}
}
