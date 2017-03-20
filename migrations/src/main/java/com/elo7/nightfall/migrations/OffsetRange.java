package com.elo7.nightfall.migrations;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class OffsetRange {

    private static final String APPLICATION = "application";
    private static final String TOPIC = "topic";
    private static final String PARTITION = "partition";
    private static final String FROM_OFFSET = "fromOffset";
    private static final String UNTIL_OFFSET = "untilOffset";
    private static final String CREATED_AT = "createdAt";

    public static void copy(Session session, String source, String destination) throws Exception {

        ResultSet offsetRanges = session.execute("SELECT * FROM " + source);
        String insertStatment = "INSERT INTO " + destination
                + "(application, topic, partition, fromOffset, untilOffset, createdAt)"
                + "VALUES(:application, :topic, :partition, :fromOffset, :untilOffset, :createdAt)";

        PreparedStatement insert = session.prepare(insertStatment);
        offsetRanges.iterator().forEachRemaining(row -> {
            BoundStatement statement = insert.bind()
                    .setString(APPLICATION, row.getString(APPLICATION))
                    .setString(TOPIC, row.getString(TOPIC))
                    .setInt(PARTITION, row.getInt(PARTITION))
                    .setLong(FROM_OFFSET, row.getLong(FROM_OFFSET))
                    .setLong(UNTIL_OFFSET, row.getLong(UNTIL_OFFSET))
                    .setDate(CREATED_AT, row.getDate(CREATED_AT));

            session.execute(statement);
        });
    }
}
