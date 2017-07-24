package com.elo7.nightfall.di.providers.kafka;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConfiguration;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConnection;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConnectionFactory;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KafkaOffsetRepository implements AutoCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOffsetRepository.class);

	private static final String QUERY_INSERT_OFFSET_RANGE = "INSERT INTO offset_ranges"
			+ "(application, topic, partition, fromOffset, untilOffset, updatedAt) "
			+ "VALUES(:application, :topic, :partition, :fromOffset, :untilOffset, :updatedAt)";

	private static final String QUERY_INSERT_OFFSET_RANGE_HISTORY = "INSERT into offset_ranges_history"
			+ "(application, topic, partition, fromOffset, untilOffset, createdAt) "
			+ "VALUES(:application, :topic, :partition, :fromOffset, :untilOffset, :createdAt) USING TTL :ttl";

	private static final String APPLICATION = "application";
	private static final String TOPIC = "topic";
	private static final String PARTITION = "partition";
	private static final String FROM_OFFSET = "fromOffset";
	private static final String UNTIL_OFFSET = "untilOffset";
	private static final String CREATED_AT = "createdAt";
	private static final String UPDATED_AT = "updatedAt";
	private static final String TTL = "ttl";

	private final CassandraConnection connection;
	private final CassandraConfiguration configuration;

	KafkaOffsetRepository(CassandraConfiguration configuration) {
		this.configuration = configuration;
		this.connection = CassandraConnectionFactory.create(configuration);
	}

	public Map<String, Map<String, Long>> findTopicOffset(@Nonnull Set<String> topics, @Nonnull String application) {
		Statement statement = QueryBuilder
				.select(TOPIC, PARTITION, UNTIL_OFFSET)
				.from("offset_ranges")
				.where(QueryBuilder.eq(APPLICATION, application));
		Map<String, Map<String, Long>> result = new HashMap<>();

		connection.getSession().execute(statement).all().stream()
				.filter(row -> topics.contains(row.getString(TOPIC)))
				.collect(Collectors.groupingBy(row -> row.getString(TOPIC)))
				.entrySet().forEach(entry -> {
			Map<String, Long> values = new HashMap<>();
			entry.getValue().forEach(row -> values.put(row.getString(PARTITION), row.getLong(UNTIL_OFFSET)));
			result.put(entry.getKey(), values);
		});

		return result;
	}

	public void persistTopics(@Nonnull List<OffsetRange> offsetRanges, @Nonnull String application) {
		if (offsetRanges.isEmpty()) {
			LOGGER.warn("Ignoring empty list of offsetRanges for application: {}.", application);
			return;
		}

		LOGGER.info("Persisting {}, application: {}.", offsetRanges, application);

		Session session = connection.getSession();
		BatchStatement batch = new BatchStatement();
		PreparedStatement insertOffset = session.prepare(QUERY_INSERT_OFFSET_RANGE);
		PreparedStatement insertOffsetHistory = session.prepare(QUERY_INSERT_OFFSET_RANGE_HISTORY);

		offsetRanges.forEach(offsetRange -> {
			batch.add(createOffsetBoundStatement(insertOffset, offsetRange, application));
			batch.add(createHistoryBoundStatement(insertOffsetHistory, offsetRange, application));
		});

		session.execute(batch);
	}

	@Override
	public void close() {
		connection.close();
	}

	private BoundStatement createOffsetBoundStatement(
			PreparedStatement statement, OffsetRange offsetRange, String application) {
		return statement.bind()
				.setString(APPLICATION, application)
				.setString(TOPIC, offsetRange.topic())
				.setInt(PARTITION, offsetRange.partition())
				.setLong(FROM_OFFSET, offsetRange.fromOffset())
				.setLong(UNTIL_OFFSET, offsetRange.untilOffset())
				.setTimestamp(UPDATED_AT, new Date());
	}

	private BoundStatement createHistoryBoundStatement(
			PreparedStatement statement, OffsetRange offsetRange, String application) {
		return statement.bind()
				.setString(APPLICATION, application)
				.setString(TOPIC, offsetRange.topic())
				.setInt(PARTITION, offsetRange.partition())
				.setLong(FROM_OFFSET, offsetRange.fromOffset())
				.setLong(UNTIL_OFFSET, offsetRange.untilOffset())
				.setTimestamp(CREATED_AT, new Date())
				.setInt(TTL, configuration.getOffsetRangeHistoryTTLInSeconds());
	}

}
