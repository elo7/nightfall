package com.elo7.nightfall.examples.repository;

import com.elo7.nightfall.examples.counter.DataPointTypeCounter;
import com.elo7.nightfall.examples.counter.daily.DataPointTypeCounterKey;
import com.elo7.nightfall.persistence.relational.DBIFactory;
import com.elo7.nightfall.persistence.relational.DataBaseConfiguration;
import com.google.inject.Inject;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Iterator;

public class RelationalRepository implements Serializable {
	private static final long serialVersionUID = 1L;
	private final DataBaseConfiguration dbConfig;

	@Inject
	public RelationalRepository(DataBaseConfiguration dbConfig) {
		this.dbConfig = dbConfig;
	}

	public void updateDataPointTypeCounter(DataPointTypeCounter dataPointTypeCounter) {

		DBI dbi = DBIFactory.create(dbConfig);

		try (Handle handle = dbi.open()) {

			RelationalDAO dao = handle.attach(RelationalDAO.class);

			if (dataPointTypeCounter != null) {
				if (dao.update(dataPointTypeCounter) == 0) {
					dao.create(dataPointTypeCounter);
				}
			}
		}
	}

	public void updateDailyDataPointTypeCounter(Tuple2<DataPointTypeCounterKey, DataPointTypeCounter> dataPointTypeCounter) {

		DBI dbi = DBIFactory.create(dbConfig);

		try (Handle handle = dbi.open()) {

			RelationalDAO dao = handle.attach(RelationalDAO.class);

			if (dataPointTypeCounter != null) {
				if (dao.updateDaily(dataPointTypeCounter._2()) == 0) {
					dao.createDaily(dataPointTypeCounter._2());
				}
			}
		}
	}

	public void updateDailyDataPointTypeCounter(Iterator<Tuple2<DataPointTypeCounterKey, DataPointTypeCounter>> dataPointTypeCounter) {

		DBI dbi = DBIFactory.create(dbConfig);

		try (Handle handle = dbi.open()) {

			RelationalDAO dao = handle.attach(RelationalDAO.class);

			dataPointTypeCounter.forEachRemaining(dataPointTupleCounter -> {
				if (dataPointTupleCounter._2() != null) {
					if (dao.updateDaily(dataPointTupleCounter._2()) == 0) {
						dao.createDaily(dataPointTupleCounter._2());
					}
				}
			});
		}
	}
}
