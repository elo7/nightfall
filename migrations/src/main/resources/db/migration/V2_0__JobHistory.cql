CREATE TABLE job_history (
	application text,
	startedAt timestamp,
	finishedAt timestamp,
	elapsedTimeInSeconds bigint,
	configurations map<text, text>,
	PRIMARY KEY(application, startedAt)
) WITH CLUSTERING ORDER BY (startedAt DESC);
