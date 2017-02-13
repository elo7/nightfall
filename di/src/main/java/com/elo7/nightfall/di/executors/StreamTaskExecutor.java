package com.elo7.nightfall.di.executors;

import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class StreamTaskExecutor implements TaskExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamTaskExecutor.class);

    private final JavaStreamingContext context;

    @Inject
    StreamTaskExecutor(JavaStreamingContext context) {
        this.context = context;
    }

    @Override
    public void runTasks() {
        try {
            LOGGER.info("Starting processing tasks.");
            context.start();
            context.awaitTermination();
        } catch (Exception e) {
            LOGGER.error("Error while processing stream", e);
            throw new RuntimeException(e);
        } finally {
            context.stop(true, true);
        }
    }

}
