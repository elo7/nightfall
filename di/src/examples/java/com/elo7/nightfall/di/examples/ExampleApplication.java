package com.elo7.nightfall.di.examples;

import com.elo7.nightfall.di.NightfallApplication;
import com.elo7.nightfall.di.providers.aws.S3RDD;

@S3RDD
public class ExampleApplication {

    public static void main(String[] args) {
        NightfallApplication.run(ExampleApplication.class, args);
    }
}
