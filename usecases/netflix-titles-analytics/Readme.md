- Author: Anshul Gautam (https://www.anshulgautam.in)

# Getting Started with Netflix Titles Analytics Application

### Overview of the application:

i) NetflixTitlesStreamingAgent in the codebase reads the netflix_titles.csv file from Kaggle, and produce records to a Kafka topic, raw-data-topic, through a custom Kafka producer NetflixKafkaProducer. Intentionally, I have added a delay of 1 second, so it makes the topic to receive data from NetflixTitlesStreamingAgent every second continuously.
ii) NetflixKafkaStreamListener in the codebase reads from raw-data-topic, applies filter logic, does transformation (stateful as well stateless) on KStream, removes certain keywords and masks them with mask character. And finally, it pushes the sanitized data to sanitized-data-topic.
iii) NetflixKafkaTransformedStreamListener reads the sanitized data from sanitized-data-topic Kafka topic.
iv) NetflixTitlesAnalyticsApplication is the starting point of this application. It stitches all the components together and starts the application. This application keeps running till all the records from the netflix_titles.csv file has been read by NetflixTitlesStreamingAgent and pushed to raw-data-topic.
v) CustomSerdes class provide custom serdes used in the application.
vi) KafkaConfiguration provides necessary configuration for KafkaStream, KafkaProducer and KafkaConsumer in the application.
vii) Log4j2 has been used in the application.

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
- Create topics
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --create --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 2 --topic raw-data-topic
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --create --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 2 --topic sanitized-data-topic
- Overview of topics used 
raw-data-topic: This topic is populated by NetflixTitlesStreamingAgent with raw data.
sanitized-data-topic: This topic is populated by NetflixKafkaStreamListener, after applying filter logic and doing transformation.
- List all created topics:
/mnt/d/DevTools/kafka_2.13-3.5.0/bin/kafka-topics.sh --list --bootstrap-server 127.0.0.1:9092
- If working with WSL2, make sure to have:
listeners=PLAINTEXT://[::1]:9092 in server.properties
The same should be used in Java code also for bootstrap server details. Otherwise with WSL2 we will get error: `Kafka TimeoutException: Topic not present in metadata after 60000 ms`. 
eg: 
public static final String BOOTSTRAP_SERVERS = "[::1]:9092";



