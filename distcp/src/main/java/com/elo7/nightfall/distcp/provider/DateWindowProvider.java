package com.elo7.nightfall.distcp.provider;

import com.elo7.nightfall.distcp.configuration.NightfallDistcpConfiguration;
import com.google.inject.Provider;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

class DateWindowProvider implements Provider<JavaRDD<String>> {

	private final NightfallDistcpConfiguration configuration;
	private final JavaSparkContext context;

	@Inject
	DateWindowProvider(NightfallDistcpConfiguration configuration, JavaSparkContext context) {
		this.configuration = configuration;
		this.context = context;
	}

	@Override
	public JavaRDD<String> get() {
		List<String> paths = configuration.getWindowDays()
				.stream()
				.map(LocalDate::toString)
				.collect(Collectors.toList());
		return context.parallelize(paths);
	}

}
