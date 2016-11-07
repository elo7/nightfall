package com.elo7.nightfall.di.commons.configuration.option;

import org.apache.commons.cli.Option;

import java.io.Serializable;
import java.util.Optional;

public interface PropertyOption<T> extends Serializable {

    String propertyName();

    String description();

    boolean isRequired();

    Optional<T> defaultValue();

    T parse(String value);

    default Option propertyOption() {
        Option option = new Option(propertyName(), true, description());

        option.setRequired(isRequired());
        option.setArgName(propertyName());

        return option;
    }
}
