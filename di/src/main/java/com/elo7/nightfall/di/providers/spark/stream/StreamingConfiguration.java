package com.elo7.nightfall.di.providers.spark.stream;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.governator.annotations.Configuration;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

/**
 * Streaming configurations:
 * <ul>
 * <li>stream.batch.interval.ms: interval in milliseconds to fetch data, default 500.</li>
 * <li>stream.checkpoint.directory: checkpoint directory, supports HDFS, S3 or File System. Examples:
 * <ul>
 * <li>S3: s3a://spark.elo7aws.com/some/path</li>
 * <li>FileSystem: /some/directory</li>
 * <li>hdfs://some/path</li>
 * </ul>
 * </li>
 * <li>stream.graceful.shutdown: when enabled all processing of the current loaded data will be complete before
 * shutdown, default true</li>
 * <li>stream.writeAheadLog: when enabled data fetched will also be persisted on checkpoints, default false.</li>
 * </ul>
 */
public class StreamingConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamingConfiguration.class);

	@Configuration("stream.batch.interval.ms")
	private long batchIntervalMS = 500;
	@Configuration("stream.checkpoint.directory")
	private String checkpointDir;
	@Configuration("stream.graceful.shutdown")
	private boolean gracefulShutdown = true;
	@Configuration("stream.writeAheadLog")
	private boolean writeAheadLog = false;
	@Configuration("stream.back.pressure.enabled")
	private boolean backPressureEnabled = true;
	@Configuration("stream.max.rate.per.second")
	private int maxRatePerSecond = 5000;
	@Configuration("stream.provider.converter")
	private String streamConverter = "com.elo7.nightfall.di.providers.spark.stream.DataPointStreamContextConverter";

	public Duration getBatchInterval() {
		return Durations.milliseconds(batchIntervalMS);
	}

	public Optional<String> getCheckpointDir() {
		return Optional.ofNullable(checkpointDir);
	}

	public String isGracefulShutdown() {
		return Boolean.toString(gracefulShutdown);
	}

	public String isWriteAheadLog() {

		// writeAheadLog can be enabled only if checkpoints are enabled
		if (getCheckpointDir().isPresent()) {
			return Boolean.toString(writeAheadLog);
		}

		LOGGER.warn("Checkpoints is disabled, so writeAheadLog is also disabled");
		return Boolean.FALSE.toString();
	}

	public String isBackPressureEnabled() {
		return Boolean.toString(backPressureEnabled);
	}

	public String getMaxRatePerSecond() {
		return String.valueOf(maxRatePerSecond);
	}

	@VisibleForTesting
	void setCheckpointDir(String checkpointDir) {
		this.checkpointDir = checkpointDir;
	}

	@VisibleForTesting
	void setWriteAheadLog(boolean writeAheadLog) {
		this.writeAheadLog = writeAheadLog;
	}

	public String getStreamConverter() {
		return streamConverter;
	}
}
