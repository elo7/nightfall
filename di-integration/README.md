# DI Integration

Integration tests for dependency injection.

The purpose of this sub-project is simply to provide a way to run integration tests to validate the dependency injection module.

### Development

* Prerequisites:
  * Kafka
  * Cassandra
* Generate the file ``nightfall.properties`` based on ``nightfall-integration.properties.sample``
* Execute the command:

```shell
./gradlew ':di-integration':run
```

### Integration

* Generate the file ``nightfall.properties`` based on ``nightfall-integration.properties.sample``
* Enable checkpoints and persistence of offsets
* Send the configuration file Zookeeper.
* To execute in integration the artifact should be submitted to the integration environment.
* Para execução em integration o artefato deverá ser submetido ao ambiente de integração.
