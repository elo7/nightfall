package com.elo7.nightfall.di.commons.configuration;

import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;

import java.util.Objects;
import java.util.Optional;

public abstract class BasePropertyOption<T> implements PropertyOption<T> {

	private static final long serialVersionUID = 1L;

	private final String propertyName;
	private final String description;
	private final boolean isRequired;
	private final T defaultValue;

	public BasePropertyOption(String propertyName, String description, boolean isRequired, T defaultValue) {
		this.propertyName = propertyName;
		this.description = description;
		this.isRequired = isRequired;
		this.defaultValue = defaultValue;
	}

	@Override
	public String propertyName() {
		return propertyName;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public boolean isRequired() {
		return isRequired;
	}

	@Override
	public Optional<T> defaultValue() {
		return Optional.ofNullable(defaultValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof BasePropertyOption) {
			BasePropertyOption<?> that = (BasePropertyOption<?>) obj;

			return Objects.equals(this.propertyName, that.propertyName);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(propertyName);
	}
}
