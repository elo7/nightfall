package com.elo7.nightfall.examples.counter;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.function.Transformer;

public class DataPointTypeCounterMapper implements Transformer<DataPoint<String>, DataPointTypeCounter> {
	private static final long serialVersionUID = 1L;

	@Override
	public DataPointTypeCounter apply(DataPoint<String> dataPoint) {
		return new DataPointTypeCounter()
				.withMessageType(dataPoint.getType())
				.withTypeCounter(1l);
	}
}
