package com.elo7.nightfall.di.providers.configuration.zookeeper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ZookeeperConfigurationTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConnectionStringIsEmpty() {
        ZookeeperConfiguration.create(" ");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConnectionStringIsNull() {
        ZookeeperConfiguration.create(null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNodePathIsEmpty() {
        ZookeeperConfiguration.create("zkQuerum:port,other:port/ ");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConnectionStringSeparatorIsMissing() {
        ZookeeperConfiguration.create("zkQuorum:port,other:port");
        fail();
    }

    @Test
    public void shouldCreateZookeeperConfiguration() {
        ZookeeperConfiguration configuration = ZookeeperConfiguration.create("zkQuorum:port,other:port/path/sub/other");
        assertEquals("zkQuorum:port,other:port", configuration.getQuorum());
        assertEquals("/path/sub/other", configuration.getNodePath());
    }
}
