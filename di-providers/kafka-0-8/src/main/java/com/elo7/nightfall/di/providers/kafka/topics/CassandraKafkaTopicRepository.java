package com.elo7.nightfall.di.providers.kafka.topics;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.persistence.cassandra.CassandraConnection;
import com.elo7.nightfall.persistence.cassandra.CassandraConnectionFactory;
import kafka.common.TopicAndPartition;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CassandraKafkaTopicRepository implements KafkaTopicRepository {

	private static final long serialVersionUID = 2L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraKafkaTopicRepository.class);

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

	private final KafkaCassandraConfiguration configuration;

	@Inject
	CassandraKafkaTopicRepository(KafkaCassandraConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Map<TopicAndPartition, Long> findTopicOffset(@Nonnull Set<String> topics, @Nonnull String application) {
		try (CassandraConnection connection = CassandraConnectionFactory.create(configuration)) {
			Statement statement = QueryBuilder
					.select(TOPIC, PARTITION, UNTIL_OFFSET)
					.from("offset_ranges")
					.where(QueryBuilder.eq(APPLICATION, application))
					.setFetchSize(configuration.getFetchSize());

			return connection.getSession().execute(statement).all().stream()
					.filter(row -> topics.contains(row.getString(TOPIC)))
					.map(row -> new Tuple2<>(
							new TopicAndPartition(row.getString(TOPIC), row.getInt(PARTITION)),
							row.getLong(UNTIL_OFFSET)))
					.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
		}
	}

	@Override
	public void persistTopics(@Nonnull List<OffsetRange> offsetRanges, @Nonnull String application) {
		if (offsetRanges.isEmpty()) {
			LOGGER.warn("Ignoring empty list of offsetRanges for application: {}.", application);
			return;
		}

		LOGGER.info("Persisting {}, application: {}.", offsetRanges, application);

		try (CassandraConnection connection = CassandraConnectionFactory.create(configuration)) {
			Session session = connection.getSession();
			BatchStatement batch = new BatchStatement();
			PreparedStatement insertOffset = session.prepare(QUERY_INSERT_OFFSET_RANGE);
			PreparedStatement insertOffsetHistory = session.prepare(QUERY_INSERT_OFFSET_RANGE_HISTORY);

			offsetRanges.forEach(offsetRange -> {
				batch.add(createOffsetBoundStatement(insertOffset, offsetRange, application));
				batch.add(createHistoryBoundStatement(insertOffsetHistory, offsetRange, application));
			});

			session.execute(batch);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private BoundStatement createOffsetBoundStatement(
			PreparedStatement statement, OffsetRange offsetRange, String application) {
		return statement.bind()
				.setString(APPLICATION, application)
				.setString(TOPIC, offsetRange.topic())
				.setInt(PARTITION, offsetRange.partition())
				.setLong(FROM_OFFSET, offsetRange.fromOffset())
				.setLong(UNTIL_OFFSET, offsetRange.untilOffset())
				.setDate(UPDATED_AT, new Date());
	}

	private BoundStatement createHistoryBoundStatement(
			PreparedStatement statement, OffsetRange offsetRange, String application) {
		return statement.bind()
				.setString(APPLICATION, application)
				.setString(TOPIC, offsetRange.topic())
				.setInt(PARTITION, offsetRange.partition())
				.setLong(FROM_OFFSET, offsetRange.fromOffset())
				.setLong(UNTIL_OFFSET, offsetRange.untilOffset())
				.setDate(CREATED_AT, new Date())
				.setInt(TTL, configuration.getOffsetRangeHistoryTTLInSeconds());
	}

}
