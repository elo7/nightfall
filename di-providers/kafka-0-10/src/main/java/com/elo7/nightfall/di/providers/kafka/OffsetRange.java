package com.elo7.nightfall.di.providers.kafka;

public class OffsetRange {

	private final String topic;
	private final int partition;
	private final long fromOffset;
	private final long untilOffset;

	OffsetRange(String topic, int partition, long fromOffset, long untilOffset) {
		this.topic = topic;
		this.partition = partition;
		this.fromOffset = fromOffset;
		this.untilOffset = untilOffset;
	}

	public String topic() {
		return topic;
	}

	public int partition() {
		return partition;
	}

	public long fromOffset() {
		return fromOffset;
	}

	public long untilOffset() {
		return untilOffset;
	}
}
