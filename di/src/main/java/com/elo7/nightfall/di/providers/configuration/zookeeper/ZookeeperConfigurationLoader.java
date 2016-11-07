package com.elo7.nightfall.di.providers.configuration.zookeeper;

import com.elo7.nightfall.di.providers.configuration.ConfigurationSchema;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ZookeeperConfigurationLoader {

    public static InputStream loadConfiguration(ConfigurationSchema schema) {
        ZookeeperConfiguration zkConfig = ZookeeperConfiguration.create(schema.getPath());

        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                .builder()
                .connectString(zkConfig.getQuorum())
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(60000);

        try (CuratorFramework client = builder.build()) {
            client.start();
            return new ByteArrayInputStream(client.getData().forPath(zkConfig.getNodePath()));
        } catch (Exception e) {
            throw new ZookeeperException("Failed to fetch configuration from " + schema.getPath(), e);
        }
    }

}
