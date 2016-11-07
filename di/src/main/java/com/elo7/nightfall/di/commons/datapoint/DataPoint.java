package com.elo7.nightfall.di.commons.datapoint;

import com.elo7.nightfall.di.commons.json.JsonParser;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class DataPoint<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String type;
	private final ZonedDateTime date;
	private final T payload;

	public DataPoint(String type, ZonedDateTime date, T payload) {
		this.type = type;
		this.date = date;
		this.payload = payload;
	}

	public String getType() {
		return type;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public T getPayload() {
		return payload;
	}

	public String toJson() {
		return JsonParser.toJson(this);
	}

	public boolean isType(String type) {
		return this.type != null && this.type.equalsIgnoreCase(type);
	}

	@Override
	public String toString() {
		return toJson();
	}
}
