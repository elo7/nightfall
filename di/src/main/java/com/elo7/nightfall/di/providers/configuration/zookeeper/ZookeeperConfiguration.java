package com.elo7.nightfall.di.providers.configuration.zookeeper;

import org.apache.commons.lang3.StringUtils;

class ZookeeperConfiguration {
	private final String quorum;
	private final String nodePath;

	public ZookeeperConfiguration(String quorum, String nodePath) {
		this.quorum = quorum;
		this.nodePath = nodePath;
	}

	public String getQuorum() {
		return quorum;
	}

	public String getNodePath() {
		return nodePath;
	}

	static ZookeeperConfiguration create(String zookeeperConfig) {

		if (StringUtils.isBlank(zookeeperConfig) || !zookeeperConfig.contains("/")) {
			throw new IllegalArgumentException("Invalid Zookeeper configuration path: " + zookeeperConfig);
		}

		String[] parts = zookeeperConfig.split("/", 2);

		if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
			throw new IllegalArgumentException("Invalid Zookeeper configuration path: " + zookeeperConfig);
		}

		return new ZookeeperConfiguration(parts[0], "/" + parts[1]);
	}

}
