
DROP TABLE offset_ranges_history;
CREATE TABLE offset_ranges_history(
	createdAt timestamp,
	application text,
	topic text,
	partition int,
	fromOffset bigint,
	untilOffset bigint,
	PRIMARY KEY(application, topic, partition, createdAt)
);
