# Nightfall Distcp

Make distributed copy of S3 data to a file system destination, it runs as a job on Spark.

Copying builds on noted backup format performed by [Secor](https://github.com/pinterest/secor), where all files of a certain date are aggregated into a single file at the destination. If a file already exists on the target, it will be removed before they start a new copy of S3.

Example, consider the following structure:

```
backup
   |
    ---- dt=2016-05-22
              |
               ---- 10000589.gz
               ---- 10000689.gz
   |
    ---- dt=2016-05-23
              |
               ---- 10000789.gz
               ---- 10000889.gz
```

Considering the destination as ``/tmp/distcp``, we have the following structure:

```
/tmp/distcp
       |
        ---- 2016-05-22.gz
        ---- 2016-05-23.gz
```


## Base configurations:

* **aws.region**: region where the bucket, optional. Example: ``aws.region=sa-east-1``.
* **aws.access.key**: AWS access key, optional. Example: ``aws.access.key=SECRET_ID``.
* **aws.secret.key**: AWS secret key, optional. Example: ``aws.secret.key=SECRET_KEY``.
* **aws.s3.bucket**: bucket from which the data will be read, required. Example: ``aws.s3.bucket=kafka.sa.elo7.com``.
* **aws.s3.path**: path where you can find the backups, required. Example: ``aws.s3.path=raw_logs/secor/backup``.
* **distcp.output.dir**: destination where will be saved the S3 backups, required. Formats:
	* ``/tmp/distcp``: local file system
	* ``hdfs://tmp/discp``: HDFS
* **distcp.window.size.days**: size of the data window in days, indicates how many days will be copied retroactively. Default: ``1``. Exemplo: ``distcp.window.size.days=30``.
* **distcp.window.date.end**: end date of the data window. Default current day. Example: ``distcp.window.date.end=2016-05-23``.
* **distcp.window.type**: Distcp implementation of the type
  * **DAY**: Performs the download of DataPoints by subtracting the number of days reported in property `distcp.window.size.days` (Default)
  * **MONTH**: Performs the download of the month DataPoints informed on property `distcp.window.date.end`

#### Example for download day 15,16 e 17 of may de 2016
```shell
distcp.window.size.days=3
distcp.window.date.end=2016-05-17
distcp.window.type=DAY
```

#### Example to download the entire month of January 2016
```shell
distcp.window.date.end=2016-01-17
distcp.window.type=MONTH
```

## Execution
```shell
./gradlew ':distcp':run
```

## Development

For execution in the development environment:
 - Generate `nightfall.properties` from the sample file: ``cp distcp/src/main/resources/nightfall.properties.sample distcp/src/main/resources/nightfall.properties``.
 - Change the required properties
