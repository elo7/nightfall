package com.elo7.nightfall.persistence.relational;

import com.google.common.base.MoreObjects;
import com.netflix.governator.annotations.Configuration;
import org.hibernate.validator.constraints.NotBlank;

public class DefaultDataBaseConfiguration implements DataBaseConfiguration {

    private static final long serialVersionUID = 2L;

    @NotBlank
    @Configuration("database.url")
    private String url;

    @NotBlank
    @Configuration("database.user")
    private String user;

    @Configuration("database.pass")
    private String password;

    @NotBlank
    @Configuration("database.driver.class")
    private String driverClass;

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("url", url)
                .add("user", user)
                .add("driverClass", driverClass)
                .toString();
    }
}
