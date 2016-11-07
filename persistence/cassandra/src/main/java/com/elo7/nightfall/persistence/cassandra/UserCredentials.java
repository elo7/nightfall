package com.elo7.nightfall.persistence.cassandra;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

public class UserCredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String password;

    private UserCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("password", password)
                .toString();
    }

    public static Optional<UserCredentials> createOptional(String name, String password) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        return Optional.of(new UserCredentials(name, password));
    }
}
