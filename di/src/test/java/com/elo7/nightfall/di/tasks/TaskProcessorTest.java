package com.elo7.nightfall.di.tasks;

import com.elo7.nightfall.di.NightfallApplication;
import org.junit.Test;

@TestRDD
public class TaskProcessorTest {

    @Test
    public void shouldProccessTestTask() {
        System.setProperty("spark.app.name", "TestTask");
        System.setProperty("spark.master", "local[2]");

        String args[] = {};
        NightfallApplication.run(TaskProcessorTest.class, args);
    }
}
