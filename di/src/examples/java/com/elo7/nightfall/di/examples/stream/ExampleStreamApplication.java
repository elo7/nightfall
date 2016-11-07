package com.elo7.nightfall.di.examples.stream;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.kafka.KafkaSimple;

@KafkaSimple
public class ExampleStreamApplication {

    public static void main(String[] args) {
        NightfallApplication.run(ExampleStreamApplication.class, args);
    }
}
