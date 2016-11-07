package com.elo7.nightfall.di.providers.file;

import com.elo7.nightfall.di.providers.ExecutorProvider;
import com.elo7.nightfall.di.providers.file.filter.FileFilterModule;
import com.elo7.nightfall.di.providers.spark.batch.DataPointBatchTaskProvider;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the main class with this annotation to read data points from Any Source. See {@link FileConfiguration} for
 * parameters.
 */
@BindingAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExecutorProvider(
		provider = FileRDDProvider.class,
		module = DataPointBatchTaskProvider.class,
		additionalModules = {FileFilterModule.class})
public @interface FileRDD {
}
