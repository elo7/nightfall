package com.elo7.nightfall.di.providers.reporter.statsd;

public interface StatsD {

	void timing(String key, long value);

	void increment(String key);

	void increment(String key, int magnitude);

	void sets(String key, long value);

	void gauges(String key, long value);

	void flush();
}
