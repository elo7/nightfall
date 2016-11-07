package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallApplication;

@KafkaSimple
public class KafkaSimpleIntegrationTest {

    public static void main(String[] args) {
        NightfallApplication.run(KafkaSimpleIntegrationTest.class, args);
    }

}
