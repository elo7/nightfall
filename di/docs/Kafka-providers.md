# Kafka Providers

Providers que suportam a leitura de mensagens do Kafka.

## Kafka Simple

[KafkaSimple](src/main/java/com/elo7/nightfall/di/providers/kafka/KafkaSimple.java) provê data points a partir da leitura de mensagens enviadas ao Kafka. É um stream.

Utiliza a Simple API do Kafka, a qual controla os offsets. Com uso de checkpoints será possível a garantia do consumo de mensagens exatamente uma vez. Porém caso haja atualização de código, há a necessidade de um novo diretório para checkpoint o que causa a perda de mensagens durante o deploy.

A vantagem do uso deste se dá em maior performance e maior garantia de processamento de mensagens, maiores detalhes em [Direct Approach](http://spark.apache.org/docs/latest/streaming-kafka-integration.html#approach-2-direct-approach-no-receivers).

**OBS:** não suporta a funcionalidade de grupos.

**Configurações:**

* **kafka.brokers**: lista de brokers para conexão ao Kafka, no formato host:port, exemplo: ``kafka-1:9092,kafka-2:9092``.
* **kafka.topics**: lista de tópicos para leitura de mensagens separado por virgula, exemplo: ``topicA,topicB,topicC``.
* **kafka.offset.persistent**: habilita persistencia dos offsets em banco (Cassandra). Default: ``false``.
* **kafka.auto.offset.reset**: o que fazer quando não há offset inicial para um tópico. Default: ``largest``. Valores possíveis:
  * **largest**: lê mensagens a partir do maior offset encontrado.
  * **smallest**: lê mensagens a partir do menor offset encontrado.
* **kafka.simple.repository.class**: implementação a ser utilizada para persistencia dos offsets, obrigatório. Default: ``com.elo7.nightfall.di.providers.kafka.topics.CassandraKafkaTopicRepository``.

** Configurações para persistencia de offsets (Cassandra): **

* **kafka.cassandra.offsetRange.history.ttl.days**: número de dias que serão mantidas as informações históricas dos offset ranges processados. Default: ``7``.
* **kafka.cassandra.offsetRange.fetch.size**: número de registro lidos a cada request em uma query. Default: ``100``.
* **kafka.cassandra.hosts**: lista de hosts para conexão com o Cassandra, obrigatório. Exemplo: ``node-a,node-b,node-c``.
* **kafka.cassandra.port**: porta de conexão com os hosts, default: ``9042``.
* **kafka.cassandra.user**: usuário de conexão, opcional.
* **kafka.cassandra.password**: senha do usuário, opcional.
* **kafka.cassandra.keyspace**: keyspace é equivalente ao schema do banco relacional. Obrigatório.
* **kafka.cassandra.datacenter**: data center name é utilizado para identificar um cluster, opcional.

**OBS**: quando a persistencia em banco dos offsets do Kafka estiver habilitada há a necessidade de criação do keyspace/schema do mesmo, ver [Migrations](../migrations/README.md).

### Dependencias

As dependencias ``nightfall-persistence-cassandra`` e ``nightfall-persistence-relational`` são provided e devem ser adicionadas com escopo compile de acordo com a opção selecionada.

Não são dependencias opcionais devido a falta de suporte para tal pelo gradle: [GRADLE-1749](https://issues.gradle.org/browse/GRADLE-1749).

## Kafka High Level

[KafkaHighLevel](src/main/java/com/elo7/nightfall/di/providers/kafka/KafkaHighLevel.java) provê data points a partir da leitura de mensagens enviadas ao Kafka. É um stream.

Utiliza a High Level API do Kafka, a qual utiliza o Zookeeper para controle de offset. Com uso de checkpoints e write ahead logs garante a o processamento de mensagens pelo menos uma vez, mensagens podem ser perdidas dependendo do ponto onde ocorra uma falha.

Como os dados lidos do Kafka são replicados no diretório de checkpoints há certa perda de performance. Maiores detalhes em [Receiver Approach](http://spark.apache.org/docs/latest/streaming-kafka-integration.html#approach-1-receiver-based-approach).

**Configurações:**

* **kafka.group**: nome do grupo de conexão ao Kafka, exemplo: ``NightfallGroup``.
* **kafka.topics.map:** lista de topicos com o número de partições do mesmo, formato: topico:partições,topico:partições, exemplo: ``topic:1,other:1``. Também aceita configuração no formato: ``topico,topic``, onde o número de partições é configurado através de ``kafka.default.topic.partitions``.
* **kafka.zookeeper:** endereço do quorum do Zookeeper, formato: host:port,host:port/chroot, exemplo: ``zookeeper-a:2181,zookeeper-b:2181/kafka``.
* **kafka.default.topic.partitions:** número default de partições de um tópico quando não informado em ``kafka.topics.map``. Default ``1``.
