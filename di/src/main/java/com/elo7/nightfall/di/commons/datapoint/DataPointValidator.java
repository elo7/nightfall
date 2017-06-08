package com.elo7.nightfall.di.commons.datapoint;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class DataPointValidator {

	public static boolean isValidForType(DataPoint<?> dataPoint, String type) {
		return isValid(dataPoint) && dataPoint.isType(type);
	}

	public static boolean isValidForAnyType(DataPoint<?> dataPoint, String... types) {
		return isValid(dataPoint) && Arrays.stream(types).anyMatch(dataPoint::isType);
	}

	/**
	 * Validate a DataPoint. Valid data points are not null, has non null payload and type.
	 *
	 * @param dataPoint DataPoint to be validated.
	 * @return True if DataPoint is valid, false otherwise.
	 */
	public static boolean isValid(DataPoint<?> dataPoint) {
		return dataPoint != null && StringUtils.isNotBlank(dataPoint.getType()) && dataPoint.getPayload() != null;
	}
}
