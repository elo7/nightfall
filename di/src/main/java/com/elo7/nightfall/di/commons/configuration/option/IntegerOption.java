package com.elo7.nightfall.di.commons.configuration.option;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

public class IntegerOption extends BasePropertyOption<Integer> {

    private static final long serialVersionUID = 1L;

    public IntegerOption(String propertyName, String description, boolean isRequired, Integer defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public IntegerOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public Integer parse(String value) {
        return value == null ? null : Integer.parseInt(value);
    }
}
