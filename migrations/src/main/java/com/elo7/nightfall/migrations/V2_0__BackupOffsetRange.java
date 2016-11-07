package com.elo7.nightfall.migrations;

import com.contrastsecurity.cassandra.migration.api.JavaMigration;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V2_0__BackupOffsetRange implements JavaMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(V2_0__BackupOffsetRange.class);

    @Override
    public void migrate(Session session) throws Exception {
        Create bkpTable = SchemaBuilder.createTable("offset_ranges_history_bkp")
                .addPartitionKey("application", DataType.text())
                .addClusteringColumn("topic", DataType.text())
                .addClusteringColumn("partition", DataType.cint())
                .addClusteringColumn("fromOffset", DataType.bigint())
                .addColumn("untilOffset", DataType.bigint())
                .addColumn("createdAt", DataType.timestamp());

        LOGGER.info("Creating backup table offset_ranges_history_bkp");
        session.execute(bkpTable);

        LOGGER.info("Copying data from offset_ranges_history to offset_ranges_history_bkp");
        OffsetRange.copy(session, "offset_ranges_history", "offset_ranges_history_bkp");

        LOGGER.info("Backup Done!");
    }
}
