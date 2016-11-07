package com.elo7.nightfall.di.providers.configuration;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

public class ConfigurationSchema {

    private final String source;
    private final String path;

    private ConfigurationSchema(String source, String path) {
        this.source = source;
        this.path = path;
    }

    public static ConfigurationSchema create(String configURL) {

        if (StringUtils.isBlank(configURL) || !configURL.contains("://")) {
            throw new IllegalArgumentException("Invalid configuration URL, excepted: 'source://path', but found "
                    + configURL);
        }

        String parts[] = configURL.split("://");

        if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
            throw new IllegalArgumentException("Invalid configuration URL, excepted: 'source://path', but found "
                    + configURL);
        }

        return new ConfigurationSchema(parts[0], parts[1]);
    }

    public String getSource() {
        return source;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("source", source)
                .add("path", path)
                .toString();
    }
}
