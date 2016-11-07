package com.elo7.nightfall.di.providers.kafka.highlevel;

import com.elo7.nightfall.di.providers.spark.stream.StreamSupplier;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import javax.inject.Inject;

public class KafkaHighLevelSupplier implements StreamSupplier {

    private static final long serialVersionUID = 1L;
    private final KafkaHighLevelConfiguration configuration;

    @Inject
    KafkaHighLevelSupplier(KafkaHighLevelConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JavaDStream<String> get(JavaStreamingContext context) {
        return KafkaUtils.createStream(
                context,
                configuration.getZookeeper(),
                configuration.getGroupId(),
                configuration.getTopicsMap())
                .map(Tuple2::_2);
    }
}
