package com.elo7.nightfall.di.aws;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.google.common.annotations.VisibleForTesting;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Optional;

public class S3Configuration implements Serializable {

	private static final long serialVersionUID = 1L;

	@VisibleForTesting
	@Configuration("aws.region")
	String awsRegion;

	@Configuration("aws.access.key")
	private String accessKey;

	@Configuration("aws.secret.key")
	private String secretKey;

	@NotBlank
	@VisibleForTesting
	@Configuration("aws.s3.path")
	String path;

	@NotBlank
	@Configuration("aws.s3.bucket")
	private String bucket;

	public Optional<Region> getAwsRegion() {
		if (StringUtils.isBlank(awsRegion)) {
			return Optional.empty();
		}

		return Optional.of(Region.getRegion(Regions.fromName(awsRegion)));
	}

	public Optional<AWSKeys> getAWSKeys() {
		return AWSKeys.create(accessKey, secretKey);
	}

	public String getS3Bucket() {
		return bucket;
	}

	public String getS3Path() {
		return path;
	}
}
