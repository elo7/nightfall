package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.Predicate;

public interface Filter<T> extends Predicate<T>, Serializable {
}
