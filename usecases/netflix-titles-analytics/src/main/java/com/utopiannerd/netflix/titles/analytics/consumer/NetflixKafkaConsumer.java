package com.utopiannerd.netflix.titles.analytics.consumer;

import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.createKafkaConsumerConfigurationMap;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.shutdownKafkaResource;
import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetflixKafkaConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetflixKafkaConsumer.class);

  private final KafkaConsumer<String, String> kafkaConsumer;
  private final String topicName;

  public NetflixKafkaConsumer(String topicName) {
    this.topicName = topicName;
    Preconditions.checkArgument(
        StringUtils.isNotBlank(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("topicName"));
    this.kafkaConsumer =
        requireNonNull(createConsumer(), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("kafkaConsumer"));
  }

  public void consume() {
    while (true) {
      ConsumerRecords<String, String> consumerRecords =
          this.kafkaConsumer.poll(Duration.ofMillis(500));

      consumerRecords.forEach(
          consumerRecord ->
              LOGGER.info(
                  "Consumer received record | Key: {} Value: {}",
                  consumerRecord.key(),
                  consumerRecord.value()));
    }
  }

  private KafkaConsumer<String, String> createConsumer() {
    Map<String, Object> kafkaConfigurationMap = createKafkaConsumerConfigurationMap();
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(kafkaConfigurationMap);
    kafkaConsumer.subscribe(Arrays.asList(topicName));
    shutdownKafkaResource(kafkaConsumer);
    return kafkaConsumer;
  }
}
