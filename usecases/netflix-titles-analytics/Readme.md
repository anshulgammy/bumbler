- Author: Anshul Gautam (https://www.anshulgautam.in)

# Getting Started with Netflix Titles Analytics Application

### Overview of the application:

### Why I didn't leverage the Twitter API in this Kafka use case:


### System Information:
- Kafka 3.5.0 (Link to download: https://archive.apache.org/dist/kafka/3.5.0/kafka_2.13-3.5.0.tgz)
- kafka-clients maven dependency used of version `3.5.1`.
```
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.5.1</version>
</dependency>
```
- JDK 17
- Netflix Titles CSV File from Kaggle(https://www.kaggle.com/datasets/shivamb/netflix-shows) has been used to simulate a data streaming source which every second pushes netflix title information to a kafka topic. This is being handled in `NetflixTitlesStreamingAgent.java`.
- Log4j2 version `2.20.0` has been used with SLf4J.

### Spinning up the Kafka Cluster:
- Start zookeeper
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/zookeeper-server-start.sh /mnt/d/DevTools/kafka_2.13-3.5.0/config/zookeeper.properties
- Start Kafka server/broker
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-server-start.sh /mnt/d/DevTools/kafka_2.13-3.5.0/config/server.properties
- Create topic
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --create --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 1 --topic raw-data-topic
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --create --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 1 --topic sanitized-data-topic
- List all created topics:
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --list --bootstrap-server 127.0.0.1:9092
- If working with WSL2, make sure to have:
listeners=PLAINTEXT://[::1]:9092 in server.properties
The same should be used in Java code also for bootstrap server details. Otherwise with WSL2 we will get error: `Kafka TimeoutException: Topic not present in metadata after 60000 ms`. 
eg: 
public static final String BOOTSTRAP_SERVERS = "[::1]:9092";



