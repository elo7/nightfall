package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.Trigger;
import scala.Tuple2;

@Component
class PyKafkaSink {

	private final NightfallConfigurations configurations;

	@Inject
	PyKafkaSink(NightfallConfigurations configurations) {
		this.configurations = configurations;
	}

	public void sink(Dataset<?> dataset) {
		sink(dataset, null);
	}

	public void sink(Dataset<?> dataset, String configPrefix) {
		if (StringUtils.isBlank(configPrefix)) {
			configPrefix = "kafka.output.";
		}

		DataStreamWriter<?> kafka = dataset.writeStream().format("kafka");
		String optionPrefix = configPrefix + "option.";

		configurations.getPropertiesWithPrefix(optionPrefix)
				.entrySet()
				.stream()
				.map(entry -> new Tuple2<>(entry.getKey().replaceFirst(optionPrefix, ""), entry.getValue()))
				.forEach(option -> kafka.option(option._1(), option._2()));
		configurations.getProperty(configPrefix + "triggerMS")
				.map(Long::parseLong)
				.map(Trigger::ProcessingTime)
				.ifPresent(kafka::trigger);
		configurations.getProperty(configPrefix + "partitions")
				.map(partitions -> partitions.split(","))
				.ifPresent(kafka::partitionBy);

		String outputMode = configurations.getProperty(configPrefix + "outputMode").orElse("update");
		kafka
				.outputMode(outputMode)
				.start();
	}
}
