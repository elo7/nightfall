package com.elo7.nightfall.persistence.relational;

import org.skife.jdbi.v2.DBI;

/**
 * Creates a DBI instance.
 */
public class DBIFactory {

    /**
     * Creates the DBI instance and assure that the jdbc driver will be loaded.
     *
     * @param configuration DataBaseConfiguration
     * @return DBI instance
     */
    public static DBI create(DataBaseConfiguration configuration) {
        try {
            Class.forName(configuration.getDriverClass());
            return new DBI(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
