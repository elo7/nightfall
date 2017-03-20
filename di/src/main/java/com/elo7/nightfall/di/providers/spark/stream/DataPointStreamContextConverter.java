package com.elo7.nightfall.di.providers.spark.stream;

import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.commons.json.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.streaming.api.java.JavaDStream;

import java.lang.reflect.Type;

@Component
public class DataPointStreamContextConverter implements SparkStreamConverter<DataPoint<String>> {
	private static final long serialVersionUID = 1L;
	private static final Type TYPE = new TypeToken<DataPoint<String>>() {}.getType();

	@Override
	public JavaDStream<DataPoint<String>> convert(JavaDStream<String> stream) {
		return stream
				.filter(StringUtils::isNotBlank)
				.map(item -> (DataPoint<String>) JsonParser.fromJson(item, TYPE))
				.filter(DataPointValidator::isValid);
	}
}
