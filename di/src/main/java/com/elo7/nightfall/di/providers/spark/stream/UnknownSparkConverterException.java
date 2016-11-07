package com.elo7.nightfall.di.providers.spark.stream;

public class UnknownSparkConverterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownSparkConverterException(String message) {
        super(message);
    }

    public UnknownSparkConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
