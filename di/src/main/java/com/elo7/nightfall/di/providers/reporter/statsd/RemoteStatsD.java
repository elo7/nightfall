package com.elo7.nightfall.di.providers.reporter.statsd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example usage:
 * <p>
 * StatsD client = new StatsD("statsd.example.com", 8125, 512);
 * // increment by 1
 * client.increment("foo.bar.baz");
 * // increment by 10
 * client.increment("foo.bar.baz", 10);
 * // sample rate
 * client.increment("foo.bar.baz", 10, .1);
 * // increment multiple keys by 1
 * client.increment("foo.bar.baz", "foo.bar.boo", "foo.baz.bar");
 * // increment multiple keys by 10 -- yeah, it's "backwards"
 * client.increment(10, "foo.bar.baz", "foo.bar.boo", "foo.baz.bar");
 * // multiple keys with a sample rate
 * client.increment(10, .1, "foo.bar.baz", "foo.bar.boo", "foo.baz.bar");
 * // Timers in milliseconds
 * client.timing("foo.bar", 123);
 * // Gauges
 * client.gauge("foo.bar", 5)
 */
class RemoteStatsD implements StatsD {

	private static Logger LOGGER = LoggerFactory.getLogger(RemoteStatsD.class);

	private final UDPSender sender;
	private final int bufferSize;
	private final String prefix;
	private final StringBuilder buffer;

	RemoteStatsD(UDPSender sender, int bufferSize, String prefix) {
		this.sender = sender;
		this.bufferSize = bufferSize;
		this.prefix = prefix;
		this.buffer = new StringBuilder(bufferSize);
	}

	@Override
	public void timing(String key, long value) {
		send(String.format("%s:%d|ms", key, value));
	}

	@Override
	public void increment(String key) {
		send(String.format("%s:%s|c", key, 1));
	}

	@Override
	public void increment(String key, int magnitude) {
		send(String.format("%s:%s|c", key, magnitude));
	}

	@Override
	public void sets(String key, long value) {
		send(String.format("%s:%s|s", key, value));
	}

	@Override
	public void gauges(String key, long value) {
		send(String.format("%s:%s|g", key, value));
	}

	@Override
	public void flush() {
		doFlush();
	}

	private void send(String stat) {
		stat = prefix + stat;

		if (stat.length() > bufferSize) {
			LOGGER.error("Metric size [{}] higher than buffer size [{}], discarding!", stat.length(), bufferSize);
			return;
		}

		if (buffer.length() + stat.length() > bufferSize) {
			doFlush();
		}

		buffer.append(stat).append("\n");
	}

	private void doFlush() {
		String stat;

		synchronized (buffer) {
			if (buffer.length() == 0) {
				return;
			}

			stat = buffer.toString();
			// clear buffer
			buffer.setLength(0);
		}

		LOGGER.debug("Sending stats:{}", stat);
		sender.send(stat);
	}
}
