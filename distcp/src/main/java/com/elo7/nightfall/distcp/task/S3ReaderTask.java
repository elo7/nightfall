package com.elo7.nightfall.distcp.task;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.elo7.nightfall.di.aws.AWSKeys;
import com.elo7.nightfall.di.aws.S3Configuration;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.elo7.nightfall.di.tasks.Task;
import com.elo7.nightfall.distcp.configuration.NightfallDistcpConfiguration;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

@Task
class S3ReaderTask implements BatchTaskProcessor<String> {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(S3ReaderTask.class);
	private static final String SLASH = "/";
	private static final int BUFFER_SIZE = 4096;

	private final NightfallDistcpConfiguration configuration;
	private final S3Configuration s3Configuration;

	@Inject
	S3ReaderTask(NightfallDistcpConfiguration configuration, S3Configuration s3Configuration) {
		this.configuration = configuration;
		this.s3Configuration = s3Configuration;
	}

	@Override
	public void process(JavaRDD<String> rdd) {
		rdd.foreach(this::performCopyFromS3);
	}

	private void performCopyFromS3(String date) throws IOException {
		s3Configuration.getAWSKeys().ifPresent(AWSKeys::setupKeysInJavaProperties);
		AmazonS3Client s3Client = new AmazonS3Client();

		s3Configuration.getAwsRegion().ifPresent(s3Client::setRegion);

		ListObjectsRequest request = new ListObjectsRequest()
				.withBucketName(s3Configuration.getS3Bucket())
				.withPrefix(getS3Path(date));
		FileSystem fileSystem = FileSystem.get(new Configuration());
		Path path = createOutPutFile(date, fileSystem);
		LOGGER.info("Merging content from {} into {}", request.getPrefix(), path);

		try (OutputStream outputStream = fileSystem.create(path)) {
			ObjectListing result = s3Client.listObjects(request);

			do {
				result.getObjectSummaries().stream()
						.map(S3ObjectSummary::getKey)
						.forEach(key -> addFile(key, s3Client, outputStream));
				// Next page of results
				result = s3Client.listNextBatchOfObjects(result);
			} while (result.isTruncated());
		}
	}

	@VisibleForTesting
	String getS3Path(String date) {
		String path = s3Configuration.getS3Path();

		if (!path.endsWith(SLASH)) {
			path = path + SLASH;
		}

		if (path.startsWith(SLASH)) {
			path = path.replaceFirst(SLASH, "");
		}

		return path + "dt=" + date + SLASH;
	}

	private void addFile(String key, AmazonS3Client s3Client, OutputStream outputStream) {
		LOGGER.debug("Fetching content from S3 bucket {} for key {}.", s3Configuration.getS3Bucket(), key);
		GetObjectRequest request = new GetObjectRequest(s3Configuration.getS3Bucket(), key);
		byte[] buffer = new byte[BUFFER_SIZE];
		int read;

		try (S3ObjectInputStream content = s3Client.getObject(request).getObjectContent()) {
			while ((read = content.read(buffer)) != NumberUtils.INTEGER_MINUS_ONE) {
				outputStream.write(buffer, NumberUtils.INTEGER_ZERO, read);
			}

			outputStream.flush();
		} catch (IOException e) {
			LOGGER.error("Failed to read from {} and or write to {}.", key, configuration.getOutputDir());
			throw new RuntimeException(e);
		}
	}

	private Path createOutPutFile(String date, FileSystem fileSystem) throws IOException {
		Path file = new Path(configuration.getOutputDir(), date + ".gz");

		fileSystem.mkdirs(file.getParent());

		if (fileSystem.exists(file)) {
			fileSystem.delete(file, false);
		}

		fileSystem.create(file);

		return file;
	}
}
