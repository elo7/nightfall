# Nightfall

Nightfall is a Valyrian steel longsword belonging to House Harlaw. It is currently wielded by Ser Harras Harlaw.

Developed initally by team Greyjoy, provides dependency injection and configuration for Spark.

![Nightfall](http://awoiaf.westeros.org/images/thumb/7/76/Nightfall.jpg/350px-Nightfall.jpg)

## Modules (subprojects)

* [Dependency Injection](di/README.md): this is the module that provides the injection of dependency.
* [Integration DI](di-integration/README.md): integration testing for injection dependence.
* [Cassandra](persistence/cassandra/README.md): module that configures the connection to Cassandra and are easier to use.
* [Relational](persistence/relation/README.md): module that configures the connection with relational databases, makes use of [JDBI](http://jdbi.org/).
* [Migrations](migrations/README.md): Migrations for DI use with Kafka Simple and Cassandra.
* [Distcp](distcp/README.md): distributed copy of the S3 data.
