package com.elo7.nightfall.migrations;

import com.contrastsecurity.cassandra.migration.api.JavaMigration;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V2_2__RestoreOffsetRange implements JavaMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(V2_0__BackupOffsetRange.class);

    @Override
    public void migrate(Session session) throws Exception {

        LOGGER.info("Copying data from offset_ranges_history_bkp to offset_ranges_history");
        OffsetRange.copy(session, "offset_ranges_history_bkp", "offset_ranges_history");
        LOGGER.info("Restore Done!");
        LOGGER.info("Dropping offset_ranges_history_bkp");
        session.execute(SchemaBuilder.dropTable("offset_ranges_history_bkp"));
    }
}
