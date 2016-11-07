package com.elo7.nightfall.di.commons.json;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class DataPointAdapter implements JsonDeserializer<DataPoint<String>> {

	@Override
	public DataPoint<String> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		if (json.getAsJsonObject().get("type") == null || json.getAsJsonObject().get("date") == null) {
			return null;
		}

		String dataPointType = json.getAsJsonObject().get("type").getAsString();
		ZonedDateTime date = ZonedDateTime.parse(json.getAsJsonObject().get("date").getAsString());

		String payload = null;
		if (json.getAsJsonObject().get("payload") != null) {
			payload = json.getAsJsonObject().get("payload").toString();
		}

		return new DataPoint<>(dataPointType, date, payload);
	}
}
