package com.elo7.nightfall.riposte.task;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.Inject;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Column;
import org.hibernate.validator.constraints.NotBlank;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RiposteConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private final NightfallConfigurations configurations;

	@NotBlank
	@Configuration("nightfall.riposte.reader.format")
	private String readerFormat;
	@Configuration("nightfall.riposte.reader.path")
	private String readerPath;

	@NotBlank
	@Configuration("nightfall.riposte.writer.format")
	private String writerFormat;
	@Configuration("nightfall.riposte.writer.path")
	private String writerPath;

	@Configuration("nightfall.riposte.query.columns")
	private String queryColumns;
	@Configuration("nightfall.riposte.query.filter")
	private String queryFilter;
	@Configuration("nightfall.riposte.query.group")
	private String queryGroup;

	@Inject
	RiposteConfiguration(NightfallConfigurations configurations) {
		this.configurations = configurations;
	}

	public Optional<String[]> query() {
		Map<String, String> columns = configurations.getPropertiesWithPrefix("nightfall.riposte.query.columns.");

		if (columns.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(columns
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.toArray(String[]::new));
	}

	public Optional<String> filter() {
		if (StringUtils.isNotBlank(queryFilter)) {
			return Optional.of(queryFilter.trim());
		}

		return Optional.empty();
	}

	public Optional<Column> groupBy() {
		if (StringUtils.isNotBlank(queryGroup)) {
			return Optional.of(new Column(queryGroup));
		}

		return Optional.empty();
	}

	public String readerFormat() {
		return readerFormat.trim();
	}

	public Map<String, String> readerOptions() {
		return getOptions("nightfall.riposte.reader.options.");
	}

	public Optional<String[]> readerPath() {
		if (StringUtils.isNotBlank(readerPath)) {
			return Optional.of(readerPath.split(","));
		}

		return Optional.empty();
	}

	public Map<String, String> writerOptions() {
		return getOptions("nightfall.riposte.writer.options.");
	}

	public Optional<String> writerPath() {
		if (StringUtils.isNotBlank(writerPath)) {
			return Optional.of(writerPath);
		}

		return Optional.empty();
	}

	public String writerFormat() {
		return writerFormat.trim();
	}

	private Map<String, String> getOptions(String prefix) {
		return configurations.getPropertiesWithPrefix(prefix)
				.entrySet()
				.stream()
				.map(entry -> new Tuple2<>(entry.getKey().replaceFirst(prefix, ""), entry.getValue()))
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
	}
}
