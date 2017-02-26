# Migrations for Nightfall DI

Migrations for Spart DI. Create tables used by Kafka Simple with Cassandra and job history.
Supported version of Cassandra until 2.2.

## HowTo

Create the keyspace to run the migrations:

```sql
CREATE KEYSPACE kafka WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor': '1'}
```

Now you can run the migrations:
```shell
./gradlew ':migrations':run -PrunArgs="-Dcassandra.migration.cluster.contactpoints=localhost -Dcassandra.migration.keyspace.name=kafka"
```
