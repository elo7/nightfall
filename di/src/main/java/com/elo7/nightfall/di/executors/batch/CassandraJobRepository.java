package com.elo7.nightfall.di.executors.batch;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.persistence.cassandra.CassandraConnection;
import com.elo7.nightfall.persistence.cassandra.CassandraConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@Component
class CassandraJobRepository implements JobHistoryRepository {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraJobRepository.class);

	private static final String QUERY_INSERT_JOB = "INSERT INTO job_history "
			+ "(application, startedAt, finishedAt, elapsedTimeInSeconds, configurations) "
			+ "VALUES(:application, :startedAt, :finishedAt, :elapsedTimeInSeconds, :configurations) "
			+ "USING TTL :ttl";

	private static final String APPLICATION = "application";
	private static final String CONFIGURATIONS = "configurations";
	private static final String STARTED_AT = "startedAt";
	private static final String FINISHED_AT = "finishedAt";
	private static final String ELAPSED_TIME = "elapsedTimeInSeconds";
	private static final String TTL = "ttl";

	private final BatchCassandraConfiguration configuration;

	@Inject
	CassandraJobRepository(BatchCassandraConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void persistHistory(@Nonnull JobHistory jobHistory) {
		LOGGER.debug("Persisting execution Job {}", jobHistory.getAppName());

		try (CassandraConnection connection = CassandraConnectionFactory.create(configuration)) {
			Session session = connection.getSession();
			PreparedStatement statement = session.prepare(QUERY_INSERT_JOB);
			BoundStatement boundStatement = statement.bind()
					.setString(APPLICATION, jobHistory.getAppName())
					.setMap(CONFIGURATIONS, jobHistory.getConfigurations())
					.setDate(STARTED_AT, jobHistory.getStartedAt())
					.setDate(FINISHED_AT, jobHistory.getFinishedAt())
					.setLong(ELAPSED_TIME, jobHistory.getElapseTimeInSeconds())
					.setInt(TTL, configuration.getTaskHistoryTTLInSeconds());
			session.executeAsync(boundStatement);
		} catch (Exception e) {
			LOGGER.error("Error on insert Job History", e);
		}
	}

}
