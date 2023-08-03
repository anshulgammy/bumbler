package com.utopiannerd.twitter.streaming.analytics.producer;

import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.ACKS_CONFIG;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.BATCH_SIZE;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.BOOTSTRAP_SERVERS;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.COMPRESSION_TYPE;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.LINGER_CONFIG;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.MAX_IN_FLIGHT_CONN;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.RAW_DATA_TOPIC;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.RETRIES_CONFIG;
import static java.util.Objects.requireNonNull;

import com.twitter.hbc.core.Client;
import com.utopiannerd.twitter.streaming.analytics.client.TwitterClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Produces raw tweet data obtained from Twitter to raw-data-topic kafka topic. */
public final class TwitterRawDataProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TwitterRawDataProducer.class);

  private final KafkaProducer<String, String> kafkaProducer;
  private final Client twitterClient;
  private final BlockingQueue<String> blockingQueue;
  private final List<String> targetTrackTerms;

  public TwitterRawDataProducer(
      BlockingQueue<String> blockingQueue, List<String> targetTrackTerms) {
    this.blockingQueue = requireNonNull(blockingQueue, "blockingQueue cannot be null");
    this.kafkaProducer = requireNonNull(createProducer(), "kafkaProducer cannot be null");
    this.targetTrackTerms = targetTrackTerms;
    if (this.targetTrackTerms == null || this.targetTrackTerms.size() == 0) {
      throw new IllegalStateException("targetTrackTerms cannot be null or empty");
    }
    this.twitterClient =
        requireNonNull(
            new TwitterClient(blockingQueue, targetTrackTerms).getTwitterClient(),
            "twitterClient cannot be null");
  }

  public void produce() {

    LOGGER.info("TwitterRawDataProducer.produce() started");

    // Establishing the Twitter connection.
    twitterClient.connect();

    // Shutdown hook configuration to close all the resources.
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  LOGGER.info("TwitterRawDataProducer is now stopping");
                  twitterClient.stop();
                  kafkaProducer.close();
                  LOGGER.info("TwitterRawDataProducer is now closed");
                }));

    // Produce Tweets to raw-data-topic topic.
    while (!twitterClient.isDone()) {

      String tweetMessage = null;

      try {
        tweetMessage = blockingQueue.poll(5, TimeUnit.SECONDS);
      } catch (InterruptedException ex) {
        LOGGER.error("Error occurred while trying to receive tweets from Twitter", ex);
        twitterClient.stop();
      }

      if (tweetMessage != null) {

        LOGGER.info("Received tweet from hose bird client: {}", tweetMessage);

        kafkaProducer.send(
            new ProducerRecord<>(RAW_DATA_TOPIC, null, tweetMessage),
            (recordMetadata, ex) -> {
              if (ex != null) {
                LOGGER.error("Error occurred while pushing to kafka topic", ex);
              }
            });
      }
    }

    LOGGER.info("TwitterRawDataProducer.produce() completed");
  }

  private KafkaProducer<String, String> createProducer() {
    Map<String, Object> kafkaConfigurationMap = createKafkaProducerConfigurationMap();
    return new KafkaProducer<>(kafkaConfigurationMap);
  }

  private Map<String, Object> createKafkaProducerConfigurationMap() {

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
}
