package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.Function;

public interface Transformer<T, R> extends Function<T, R>, Serializable {
}
