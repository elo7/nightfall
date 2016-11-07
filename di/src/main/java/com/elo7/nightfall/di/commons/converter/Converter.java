package com.elo7.nightfall.di.commons.converter;

import java.io.Serializable;

public interface Converter<S, D> extends Serializable {

    D convert(S source);
}
