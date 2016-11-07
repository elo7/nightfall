package com.elo7.nightfall.persistence.relational;

import com.google.inject.ImplementedBy;

import java.io.Serializable;

@ImplementedBy(DefaultDataBaseConfiguration.class)
public interface DataBaseConfiguration extends Serializable {

    String getUrl();

    String getUser();

    String getPassword();

    String getDriverClass();
}
