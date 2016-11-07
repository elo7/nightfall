package com.elo7.nightfall.di.providers.reporter;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ReporterModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporterModule.class);

    private final ClasspathScanner scanner;

    @Inject
    ReporterModule(ClasspathScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    protected void configure() {
        bind(ReporterFactory.class)
                .toProvider(ReporterFactoryProvider.class)
                .in(LazySingletonScope.get());

        LOGGER.info("Binding Implementations for reporters");
        Multibinder<ReporterFactory> binder = Multibinder.newSetBinder(binder(), ReporterFactory.class);

        scanner
                .getClasses()
                .stream()
                .filter(this::filer)
                .forEach(clazz -> bindImplementation(clazz, binder));
    }

    @SuppressWarnings("unchecked")
    private void bindImplementation(Class<?> clazz, Multibinder<ReporterFactory> binder) {
        binder.addBinding().to((Class) clazz).in(LazySingletonScope.get());
    }

    private boolean filer(Class<?> clazz) {
        return ReporterFactory.class.isAssignableFrom(clazz);
    }
}
