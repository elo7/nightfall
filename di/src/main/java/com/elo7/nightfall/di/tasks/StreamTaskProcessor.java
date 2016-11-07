package com.elo7.nightfall.di.tasks;

import org.apache.spark.streaming.api.java.JavaDStream;

import java.io.Serializable;

/**
 * Any stream task must implement this interface and should be annotated with {@link Task}.
 */
public interface StreamTaskProcessor<T> extends Serializable {

	/**
	 * Perform the execution of the given dStream, this stream is already cached.
	 *
	 * @param dStream cached stream.
	 */
	void process(JavaDStream<T> dStream);
}
