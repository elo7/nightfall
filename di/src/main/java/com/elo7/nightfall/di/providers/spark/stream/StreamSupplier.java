package com.elo7.nightfall.di.providers.spark.stream;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;

public interface StreamSupplier extends Serializable {

    JavaDStream<String> get(JavaStreamingContext context);
}
