package com.elo7.nightfall.examples.repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.persistence.cassandra.CassandraConnection;
import scala.Tuple2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.incr;

@Component
public class CassandraRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String INSERT_MESSAGES_EXAMPLES = "INSERT INTO messages_examples (createdAt, messageDate, messageType) VALUES (:createdAt, :messageDate, :messageType)";

	public void persistMyRDDExample(Iterator<DataPoint<String>> dataPoints, CassandraConnection cassandraConnection) {
		Session session = cassandraConnection.getSession();
		BatchStatement batch = new BatchStatement();
		PreparedStatement statement = session.prepare(INSERT_MESSAGES_EXAMPLES);
		LocalDateTime now = LocalDateTime.now();
		dataPoints.forEachRemaining(dataPoint ->
				batch.add(statement.bind()
						.setString("createdAt", now.toString())
						.setString("messageDate", dataPoint.getDate().toLocalDateTime().toString())
						.setString("messageType", dataPoint.getType())
				)
		);
		session.execute(batch);
	}

	public void persistMyRDDPairExample(Iterator<Tuple2<String, Long>> typesCounters, CassandraConnection cassandraConnection) {
		Session session = cassandraConnection.getSession();
		BatchStatement batch = new BatchStatement();
		typesCounters.forEachRemaining(typeCounter -> createStatement(typeCounter) );
		session.execute(batch);
	}

	private Statement createStatement(Tuple2<String, Long> typeCounter) {
		Statement statement = QueryBuilder.update("examples", "type_messages_counter")
				.with(incr("typeCounter", typeCounter._2()))
				.where(eq("messageType", typeCounter._1()));

		return statement;
	}

}
