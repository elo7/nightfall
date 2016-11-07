# Migrations for Nightfall DI

Migrations for Spart DI. Create tables used by Kafka Simple with Cassandra and job history.

## HowTo

Create the keyspace to run the migrations:

```sql
CREATE KEYSPACE kafka WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor': '1'}
```

Now you can run the migrations:
```shell
gradle ':migrations':run -PrunArgs="-Dcassandra.migration.cluster.contactpoints=cassandra -Dcassandra.migration.keyspace.name=kafka"
```
