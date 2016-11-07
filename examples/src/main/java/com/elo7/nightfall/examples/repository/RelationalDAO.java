package com.elo7.nightfall.examples.repository;

import com.elo7.nightfall.examples.counter.DataPointTypeCounter;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface RelationalDAO {
	@SqlUpdate("INSERT INTO type_messages_counter (messageType, typeCounter) "
			+ "VALUES (:messageType, :typeCounter)")
	int create(@BindBean DataPointTypeCounter dataPointType);

	@SqlUpdate("UPDATE type_messages_counter SET typeCounter = typeCounter + :typeCounter "
			+ "WHERE messageType = :messageType")
	int update(@BindBean DataPointTypeCounter dataPointType);

	@SqlUpdate("INSERT INTO daily_type_messages_counter (createdAt, messageType, typeCounter) "
			+ "VALUES (:createdAt, :messageType, :typeCounter)")
	int createDaily(@BindBean DataPointTypeCounter dataPointType);

	@SqlUpdate("UPDATE daily_type_messages_counter SET typeCounter = typeCounter + :typeCounter "
			+ "WHERE createdAt = :createdAt AND messageType = :messageType")
	int updateDaily(@BindBean DataPointTypeCounter dataPointType);
}
