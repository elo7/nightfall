package com.elo7.nightfall.di.function;

import java.io.Serializable;

public interface Consumer<T> extends Serializable {

	void apply(T t);
}
