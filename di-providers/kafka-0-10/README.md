# Nightfall Kafka

Simplifies kafka configuration and provides some factories for Kafka sources.

**OBS:** only supports Structure Streams.

## Configurarions:

All properties starting with ``spark.kafka`` will be used with Kafka source, exemple:

```java
spark.kafka.subscribe=marketplace_events
spark.kafka.kafka.bootstrap.servers=localhost:9092
```

Kafka configurations are found on [Structured Streaming + Kafka Integration Guide](http://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html).
