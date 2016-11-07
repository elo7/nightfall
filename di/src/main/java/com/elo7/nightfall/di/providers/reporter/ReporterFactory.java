package com.elo7.nightfall.di.providers.reporter;

import java.io.Serializable;

public interface ReporterFactory extends Serializable {

    void send(String application, ApplicationType applicationType);
}
