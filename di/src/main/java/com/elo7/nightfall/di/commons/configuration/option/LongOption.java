package com.elo7.nightfall.di.commons.configuration.option;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

public class LongOption extends BasePropertyOption<Long> {

    private static final long serialVersionUID = 1L;

    public LongOption(String propertyName, String description, boolean isRequired, Long defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public LongOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public Long parse(String value) {
        return value == null ? null : Long.parseLong(value);
    }
}
