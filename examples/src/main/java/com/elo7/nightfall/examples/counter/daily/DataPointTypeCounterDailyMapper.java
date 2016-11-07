package com.elo7.nightfall.examples.counter.daily;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.function.Transformer;
import com.elo7.nightfall.examples.counter.DataPointTypeCounter;
import scala.Tuple2;

import java.time.temporal.ChronoUnit;

public class DataPointTypeCounterDailyMapper implements Transformer<DataPoint<String>, Tuple2<DataPointTypeCounterKey, DataPointTypeCounter>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple2<DataPointTypeCounterKey, DataPointTypeCounter> apply(DataPoint<String> dataPoint) {
		DataPointTypeCounter counter = new DataPointTypeCounter()
				.withCreatedAt(dataPoint.getDate().truncatedTo(ChronoUnit.DAYS))
				.withMessageType(dataPoint.getType())
				.withTypeCounter(1l);

		return new Tuple2<>(new DataPointTypeCounterKey(counter.getCreatedAt(), counter.getMessageType()), counter);
	}
}
