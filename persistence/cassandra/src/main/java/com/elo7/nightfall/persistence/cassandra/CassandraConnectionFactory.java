package com.elo7.nightfall.persistence.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create CassandraConnection. The creation should be done on foreachrdd.
 * For configuration settings see: DefaultCassandraConfiguration.
 */
public class CassandraConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraConnectionFactory.class);

    /**
     * Creates a connection with Cassandra. Cluster and Session are created.
     *
     * @param configuration CassandraConfiguration to create a CassandraConnection
     * @return CassandraConnection
     */
    public static CassandraConnection create(CassandraConfiguration configuration) {
        Cluster.Builder builder = Cluster.builder()
                .addContactPoints(configuration.getConnectionPoints())
                .withPort(configuration.getPort());

        configuration.getDataCenterName().ifPresent(
                dataCenter -> builder.withLoadBalancingPolicy(
                        DCAwareRoundRobinPolicy.builder().withLocalDc(dataCenter).build())
        );

        configuration.getUserCredentials().ifPresent(
                user -> builder.withAuthProvider(new PlainTextAuthProvider(user.getName(), user.getPassword()))
        );

        LOGGER.info("Connecting to Cassandra Cluster");
        Cluster cluster = builder.build();
        LOGGER.info("Creating Cassandra Session");
        Session session = cluster.connect(configuration.getKeySpace());

        return new CassandraConnection(session, cluster);
    }
}
