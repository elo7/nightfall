package com.elo7.nightfall.examples;

import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.di.tasks.TaskProcessor;
import org.apache.spark.api.java.JavaRDD;

@Task
public class ExampleTask implements TaskProcessor {

    private static final long serialVersionUID = 1L;

    @Override
    public void process(JavaRDD<String> rdd) {
        rdd.collect().forEach(System.out::println);
    }
}
