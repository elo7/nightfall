package com.elo7.nightfall.di.aws;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * AWS keys. Hold aws access key and AWS secret key.
 */
public class AWSKeys implements Serializable {

	private static final long serialVersionUID = -8338757298337905900L;

	private final String accessId;
	private final String secret;

	private AWSKeys(String accessId, String secret) {
		this.accessId = accessId;
		this.secret = secret;
	}

	public String getAccessId() {
		return accessId;
	}

	public String getSecret() {
		return secret;
	}

	public static Optional<AWSKeys> create(String accessId, String secret) {
		if (StringUtils.isNotBlank(accessId) && StringUtils.isNotBlank(secret)) {
			return Optional.of(new AWSKeys(accessId, secret));
		}

		return Optional.empty();
	}

	public static void setupKeysInJavaProperties(AWSKeys keys) {
		System.setProperty("aws.accessKeyId", keys.getAccessId());
		System.setProperty("aws.secretKey", keys.getSecret());
	}
}
