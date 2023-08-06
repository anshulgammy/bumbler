package com.utopiannerd.netflix.titles.analytics.util;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.ACKS_CONFIG;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.APPLICATION_ID;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.BATCH_SIZE;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.BOOTSTRAP_SERVERS;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.COMPRESSION_TYPE;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.LINGER_CONFIG;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.MAX_IN_FLIGHT_CONN;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.RETRIES_CONFIG;

import com.utopiannerd.netflix.titles.analytics.serdes.CustomSerdes;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KafkaUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUtil.class);

  public static final UnaryOperator<String> NON_NULL_OR_EMPTY_CHECK_MESSAGE =
      (str) -> String.format("%s cannot be null or empty", str);

  public static Map<String, Object> createKafkaProducerConfigurationMap() {

    Map<String, Object> kafkaConfigurationMap = new HashMap<>();

    kafkaConfigurationMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    kafkaConfigurationMap.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    kafkaConfigurationMap.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    kafkaConfigurationMap.put(ProducerConfig.CLIENT_ID_CONFIG, APPLICATION_ID);

    // Configuration related to creation of safe Producer
    kafkaConfigurationMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    kafkaConfigurationMap.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG);
    kafkaConfigurationMap.put(ProducerConfig.RETRIES_CONFIG, RETRIES_CONFIG);
    kafkaConfigurationMap.put(
        ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, MAX_IN_FLIGHT_CONN);

    // Additional configuration for high throughput.
    kafkaConfigurationMap.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE);
    kafkaConfigurationMap.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_CONFIG);
    kafkaConfigurationMap.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE);

    return kafkaConfigurationMap;
  }

  public static Map<String, Object> createKafkaConsumerConfigurationMap() {

    Map<String, Object> kafkaConfigurationMap = new HashMap<>();

    kafkaConfigurationMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    kafkaConfigurationMap.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    kafkaConfigurationMap.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    kafkaConfigurationMap.put(ConsumerConfig.GROUP_ID_CONFIG, APPLICATION_ID);
    kafkaConfigurationMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    return kafkaConfigurationMap;
  }

  public static Map<String, Object> createKafkaStreamConfigurationMap() {

    Map<String, Object> kafkaConfigurationMap = new HashMap<>();

    kafkaConfigurationMap.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    kafkaConfigurationMap.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
    kafkaConfigurationMap.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    kafkaConfigurationMap.put(
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    return kafkaConfigurationMap;
  }

  public static Map<String, Object> createKafkaTransformedStreamConfigurationMap() {

    Map<String, Object> kafkaConfigurationMap = new HashMap<>();

    kafkaConfigurationMap.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    kafkaConfigurationMap.put(StreamsConfig.APPLICATION_ID_CONFIG, "testKS");
    kafkaConfigurationMap.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    kafkaConfigurationMap.put(
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        CustomSerdes.netflixTitleSerdes().getClass());

    return kafkaConfigurationMap;
  }

  public static void shutdownKafkaResource(Object resource) {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (resource instanceof KafkaStreams) {
                    LOGGER.info("Stopping KafkaStreams");
                    KafkaStreams kafkaStreams = (KafkaStreams) resource;
                    kafkaStreams.close();
                    LOGGER.info("Stopping KafkaStreams completed");
                  } else if (resource instanceof KafkaConsumer) {
                    LOGGER.info("Stopping KafkaConsumer");
                    KafkaConsumer kafkaConsumer = (KafkaConsumer) resource;
                    kafkaConsumer.close();
                    LOGGER.info("Stopping KafkaConsumer completed");
                  } else if (resource instanceof KafkaProducer) {
                    LOGGER.info("Stopping KafkaProducer");
                    KafkaProducer kafkaProducer = (KafkaProducer) resource;
                    kafkaProducer.close();
                    LOGGER.info("Stopping KafkaProducer completed");
                  }
                }));
  }
}
