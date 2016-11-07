package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.BiPredicate;

public interface BiFilter<T, U> extends BiPredicate<T, U>, Serializable {
}
