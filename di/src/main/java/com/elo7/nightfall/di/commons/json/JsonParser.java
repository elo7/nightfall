package com.elo7.nightfall.di.commons.json;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class JsonParser implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(new TypeToken<DataPoint<String>>() {}.getType(), new DataPointAdapter())
            .create();

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonContent, Type type) {
        try {
            return (T) gson.fromJson(jsonContent, type);
        } catch (JsonParseException e) {
            LOGGER.error("Failed to parse json: {}", jsonContent, e);
            return null;
        }
    }

    public static <T> T fromJson(String jsonContent, Class<T> clazz) {
        try {
            return gson.fromJson(jsonContent, clazz);
        } catch (JsonParseException e) {
            LOGGER.error("Failed to parse json: {}", jsonContent, e);
            return null;
        }
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
