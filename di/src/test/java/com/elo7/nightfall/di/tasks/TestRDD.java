package com.elo7.nightfall.di.tasks;

import com.elo7.nightfall.di.providers.ExecutorProvider;
import com.elo7.nightfall.di.providers.spark.batch.DataPointBatchTaskProvider;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@BindingAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExecutorProvider(
		provider = TestRDDProvider.class,
		module = DataPointBatchTaskProvider.class)
public @interface TestRDD {
}
