package com.elo7.nightfall.di.tasks;

import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;

/**
 * Any batch task should implement this interface and should be annotated with {@link Task}.
 */
public interface BatchTaskProcessor<T> extends Serializable {

	/**
	 * Process the given rdd. The rdd is already cached.
	 *
	 * @param rdd cached rdd.
	 */
	void process(JavaRDD<T> rdd);
}
