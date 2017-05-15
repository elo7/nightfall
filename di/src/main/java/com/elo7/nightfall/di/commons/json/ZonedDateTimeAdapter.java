package com.elo7.nightfall.di.commons.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

	@Override
	public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return ZonedDateTime.parse(json.getAsString());
	}

	@Override
	public JsonElement serialize(ZonedDateTime src, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}
}
