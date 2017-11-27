package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.commons.json.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class OffsetRangeConverter implements BiFunction<String, String, List<OffsetRange>> {

	private static final Type TYPE = new TypeToken<Map<String, Map<String, Long>>>() {
	}.getType();

	@Override
	public List<OffsetRange> apply(String starting, String ending) {
		Map<String, Map<String, Long>> startingOffset = parse(starting);
		Map<String, Map<String, Long>> endingOffset = parse(ending);
		List<OffsetRange> offsetRanges = new LinkedList<>();

		endingOffset.forEach((topic, partitionOffset) -> {
			Map<String, Long> startPartitionOffset = startingOffset.getOrDefault(topic, Collections.emptyMap());

			partitionOffset.forEach((partition, endOffset) -> {
				long startOffset = startPartitionOffset.getOrDefault(partition, 0L);
				OffsetRange offsetRange = new OffsetRange(topic, Integer.parseInt(partition), startOffset, endOffset);
				offsetRanges.add(offsetRange);
			});

		});

		return offsetRanges;
	}

	private Map<String, Map<String, Long>> parse(String offsetRange) {
		Map<String, Map<String, Long>> parsed = JsonParser.fromJson(offsetRange, TYPE);

		if (parsed == null) {
			return Collections.emptyMap();
		}

		return parsed;
	}

}
