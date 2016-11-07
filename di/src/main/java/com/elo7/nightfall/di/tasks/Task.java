package com.elo7.nightfall.di.tasks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spark task must be annotated by this annotation for injection and must implement {@link BatchTaskProcessor} or
 * {@link StreamTaskProcessor}.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
}
