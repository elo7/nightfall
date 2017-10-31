package com.elo7.nightfall.di.providers.kafka;

import com.google.common.base.Objects;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final OffsetRange that = (OffsetRange) o;
		return partition == that.partition &&
				fromOffset == that.fromOffset &&
				untilOffset == that.untilOffset &&
				Objects.equal(topic, that.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(topic, partition, fromOffset, untilOffset);
	}
}
