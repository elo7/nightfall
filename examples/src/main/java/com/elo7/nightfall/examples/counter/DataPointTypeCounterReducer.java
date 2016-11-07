package com.elo7.nightfall.examples.counter;

import com.elo7.nightfall.di.function.Reducer;

public class DataPointTypeCounterReducer implements Reducer<DataPointTypeCounter> {
	private static final long serialVersionUID = 1L;

	@Override
	public DataPointTypeCounter apply(DataPointTypeCounter first, DataPointTypeCounter second) {
		return new DataPointTypeCounter()
				.withCreatedAt(first.getCreatedAt())
				.withMessageType(first.getMessageType())
				.withTypeCounter(first.getTypeCounter() + second.getTypeCounter());
	}
}
