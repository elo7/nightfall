package com.elo7.nightfall.di.commons.configuration.option;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

public class StringOption extends BasePropertyOption<String> {

    private static final long serialVersionUID = 1L;

    public StringOption(String propertyName, String description, boolean isRequired, String defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public StringOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public String parse(String value) {
        return value;
    }
}
