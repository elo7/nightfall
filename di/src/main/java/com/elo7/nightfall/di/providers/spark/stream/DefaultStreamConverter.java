package com.elo7.nightfall.di.providers.spark.stream;

import com.elo7.nightfall.di.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.streaming.api.java.JavaDStream;

@Component
public class DefaultStreamConverter implements SparkStreamConverter<String> {

	@Override
	public JavaDStream<String> convert(JavaDStream<String> stream) {
		return stream.filter(StringUtils::isNotEmpty);
	}
}
