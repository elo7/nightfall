package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.Supplier;

public interface Provider<T> extends Supplier<T>, Serializable {
}
