package com.elo7.nightfall.di.providers.kafka;

import org.apache.spark.sql.streaming.StreamingQueryListener;

import java.util.stream.Stream;

public class KafkaOffsetPersistentListener extends StreamingQueryListener {

	private static final String KAFKA_SOURCE_PREFIX = "KafkaSource";
	private final KafkaOffsetRepository repository;
	private final String application;
	private final OffsetRangeConverter offsetRangeConverter;

	KafkaOffsetPersistentListener(KafkaOffsetRepository repository, String application, OffsetRangeConverter offsetRangeConverter) {
		this.repository = repository;
		this.application = application;
		this.offsetRangeConverter = offsetRangeConverter;
	}

	@Override
	public void onQueryStarted(QueryStartedEvent queryStartedEvent) {
	}

	@Override
	public void onQueryProgress(QueryProgressEvent queryProgressEvent) {
		Stream.of(queryProgressEvent.progress().sources())
				// There are always an ending offset, but the number of input rows indicate when there was some processing
				.filter(source -> source.numInputRows() > 0)
				.filter(source -> source.description().startsWith(KAFKA_SOURCE_PREFIX))
				.map(source -> offsetRangeConverter.apply(source.startOffset(), source.endOffset()))
				.forEach(offsetRanges -> repository.persistTopics(offsetRanges, application));
	}

	@Override
	public void onQueryTerminated(QueryTerminatedEvent queryTerminatedEvent) {

	}
}
