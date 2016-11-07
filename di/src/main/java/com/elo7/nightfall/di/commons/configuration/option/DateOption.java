package com.elo7.nightfall.di.commons.configuration.option;

import java.time.ZonedDateTime;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

public class DateOption extends BasePropertyOption<ZonedDateTime> {

    private static final long serialVersionUID = 1L;

    public DateOption(String propertyName, String description, boolean isRequired, ZonedDateTime defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public DateOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public ZonedDateTime parse(String value) {
        return ZonedDateTime.parse(value);
    }
}
