package com.utopiannerd.netflix.titles.analytics.producer;

import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.createKafkaProducerConfigurationMap;
import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetflixKafkaProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetflixKafkaProducer.class);

  private final KafkaProducer<String, String> kafkaProducer;
  private final String topicName;

  public NetflixKafkaProducer(String topicName) {
    this.topicName = topicName;
    Preconditions.checkArgument(
        StringUtils.isNotBlank(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("topicName"));
    this.kafkaProducer =
        requireNonNull(createProducer(), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("kafkaProducer"));
  }

  public KafkaProducer<String, String> getKafkaProducer() {
    return this.kafkaProducer;
  }

  public void produce(String record) {
    this.kafkaProducer.send(
        new ProducerRecord<>(topicName, String.valueOf(UUID.randomUUID()), record),
        (recordMetadata, ex) -> {
          if (ex != null) {
            ex.printStackTrace();
            LOGGER.error("Error occurred while pushing to kafka topic", ex);
          }
        });
  }

  private KafkaProducer<String, String> createProducer() {
    Map<String, Object> kafkaConfigurationMap = createKafkaProducerConfigurationMap();
    return new KafkaProducer<>(kafkaConfigurationMap);
  }
}
