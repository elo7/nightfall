package com.elo7.nightfall.di.commons.configuration.option;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

public class BooleanOption extends BasePropertyOption<Boolean> {

    private static final long serialVersionUID = 1L;

    public BooleanOption(String propertyName, String description, boolean isRequired, Boolean defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public BooleanOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public Boolean parse(String value) {
        return Boolean.parseBoolean(value);
    }
}
