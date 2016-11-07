package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.Consumer;

public interface Emiter<T> extends Consumer<T>, Serializable {
}
