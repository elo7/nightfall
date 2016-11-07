package com.elo7.nightfall.di.commons.configuration.option;

import com.elo7.nightfall.di.commons.configuration.BasePropertyOption;

import java.math.BigDecimal;

public class BigDecimalOption extends BasePropertyOption<BigDecimal> {

    private static final long serialVersionUID = 1L;

    public BigDecimalOption(String propertyName, String description, boolean isRequired, BigDecimal defaultValue) {
        super(propertyName, description, isRequired, defaultValue);
    }

    public BigDecimalOption(String propertyName, String description, boolean isRequired) {
        super(propertyName, description, isRequired, null);
    }

    @Override
    public BigDecimal parse(String value) {
        return value == null ? null : new BigDecimal(value);
    }
}
