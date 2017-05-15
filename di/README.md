# DI - dependency injection

This module provides dependency injection to Spark.

The injection is limited to the driver, that is, you can not perform the injection in the execution of jobs because of the need for serialization of objects.

[Examples](https://github.com/elo7/nightfall/tree/nightfall-os/examples), enjoy :)

## Building

To compile the project just (integration tests are not performed because of lack of provision thereof):

```shell
cp di/src/main/resources/nightfall.properties.sample di/src/main/resources/nightfall.properties
./gradlew build -PskipIntegration
```

Learn more about [How It Works](https://github.com/elo7/nightfall/wiki/how-it-works) and [How To Use](https://github.com/elo7/nightfall/wiki/how-to-use)
