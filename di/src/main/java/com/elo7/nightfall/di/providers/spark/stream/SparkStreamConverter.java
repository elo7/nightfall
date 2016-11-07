package com.elo7.nightfall.di.providers.spark.stream;

import org.apache.spark.streaming.api.java.JavaDStream;

import java.io.Serializable;

public interface SparkStreamConverter<T> extends Serializable {

	JavaDStream<T> convert(JavaDStream<String> stream);
}
