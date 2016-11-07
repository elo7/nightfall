package com.elo7.nightfall.di.providers.configuration.zookeeper;

public class ZookeeperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ZookeeperException() {
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }
}
