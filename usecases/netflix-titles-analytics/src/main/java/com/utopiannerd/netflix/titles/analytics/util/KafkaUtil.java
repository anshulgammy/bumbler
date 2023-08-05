package com.utopiannerd.netflix.titles.analytics.util;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.ACKS_CONFIG;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.BATCH_SIZE;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.BOOTSTRAP_SERVERS;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.COMPRESSION_TYPE;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.GROUP_ID;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.LINGER_CONFIG;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.MAX_IN_FLIGHT_CONN;
import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.RETRIES_CONFIG;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public final class KafkaUtil {

  public static final UnaryOperator<String> NON_NULL_OR_EMPTY_CHECK_MESSAGE =
      (str) -> String.format("%s cannot be null or empty", str);

  public static Map<String, Object> createKafkaProducerConfigurationMap() {

    Map<String, Object> kafkaConfigurationMap = new HashMap<>();

    kafkaConfigurationMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    kafkaConfigurationMap.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    kafkaConfigurationMap.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

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
    kafkaConfigurationMap.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    kafkaConfigurationMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    return kafkaConfigurationMap;
  }
}
