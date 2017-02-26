CREATE TABLE offset_ranges(
	updatedAt timestamp,
	application text,
	topic text,
	partition int,
	fromOffset bigint,
	untilOffset bigint,
	PRIMARY KEY(application, topic, partition)
);

CREATE TABLE offset_ranges_history(
	createdAt timestamp,
	application text,
	topic text,
	partition int,
	fromOffset bigint,
	untilOffset bigint,
	PRIMARY KEY(application, topic, partition, createdAt)
);
