package com.elo7.nightfall.di.providers.file;

import com.elo7.nightfall.di.aws.AWSKeys;
import com.netflix.governator.annotations.Configuration;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;

/**
 * File configuration parameters:
 * <ul>
 * <li>file.s3.access.key: access key for AWS, optional.</li>
 * <li>file.s3.secret.key: secret key for AWS, optional.</li>
 * <li>file.source: Source path, supports wildcards, example: /some/path/*.gz</li>
 * </ul>
 */
public class FileConfiguration {

	@Configuration("file.s3.access.key")
	private String accessKey;
	@Configuration("file.s3.secret.key")
	private String secretKey;
	@NotBlank
	@Configuration("file.source")
	private String source;
	@Configuration("file.filter")
	private String fileFilter;

	public Optional<AWSKeys> getAWSKeys() {
		return AWSKeys.create(accessKey, secretKey);
	}

	public String getSource() {
		return source;
	}
}
