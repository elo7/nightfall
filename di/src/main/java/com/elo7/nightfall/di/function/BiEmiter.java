package com.elo7.nightfall.di.function;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface BiEmiter<T, U> extends BiConsumer<T, U>, Serializable {
}


