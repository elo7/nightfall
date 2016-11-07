package com.elo7.nightfall.di.providers;

import com.elo7.nightfall.di.AbstractNightfallModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutorProvider {

	Class<? extends Provider<?>> provider();

	Class<? extends AbstractNightfallModule> module();

	Class<? extends AbstractModule>[] additionalModules() default {};

}
