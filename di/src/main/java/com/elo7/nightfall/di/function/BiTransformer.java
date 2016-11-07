package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface BiTransformer<T, U, R> extends BiFunction<T, U, R>, Serializable {
}
