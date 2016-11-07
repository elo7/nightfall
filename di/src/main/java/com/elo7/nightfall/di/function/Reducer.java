package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.BinaryOperator;

public interface Reducer<T> extends BinaryOperator<T>, Serializable {
}
